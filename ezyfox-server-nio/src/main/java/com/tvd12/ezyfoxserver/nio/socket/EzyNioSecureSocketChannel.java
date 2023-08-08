package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.socket.EzySecureChannel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;

public class EzyNioSecureSocketChannel
    extends EzyNioSocketChannel
    implements EzySecureChannel {

    private SSLEngine engine;
    private ByteBuffer netBuffer;
    @Getter
    private boolean handshaked;
    private int appBufferSize;
    private int netBufferSize;
    private final SSLContext sslContext;
    private final int sslHandshakeTimeout;
    @Getter
    private final Object packLock = new Object();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public EzyNioSecureSocketChannel(
        SocketChannel channel,
        SSLContext sslContext,
        int sslHandshakeTimeout
    ) {
        super(channel);
        this.sslContext = sslContext;
        this.sslHandshakeTimeout = sslHandshakeTimeout;
    }

    @SuppressWarnings("MethodLength")
    public void handshake() throws IOException {
        engine = sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        engine.beginHandshake();

        SSLSession session = engine.getSession();
        appBufferSize = session.getApplicationBufferSize();
        netBufferSize = session.getPacketBufferSize();
        netBuffer = ByteBuffer.allocate(netBufferSize);
        ByteBuffer appBuffer = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerNetData = ByteBuffer.allocate(netBufferSize);

        SSLEngineResult result;
        SSLEngineResult.HandshakeStatus handshakeStatus = engine.getHandshakeStatus();
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + sslHandshakeTimeout;
        while (handshakeStatus != FINISHED && handshakeStatus != NOT_HANDSHAKING) {
            currentTime = System.currentTimeMillis();
            if (currentTime >= endTime) {
                throw new SSLException("Timeout");
            }
            switch (handshakeStatus) {
                case NEED_UNWRAP:
                    int readBytes = channel.read(peerNetData);
                    if (readBytes < 0) {
                        if (engine.isInboundDone() && engine.isOutboundDone()) {
                            throw new SSLException(
                                "status is NEED_UNWRAP " +
                                    "while inbound and outbound done"
                            );
                        }
                        try {
                            engine.closeInbound();
                        } catch (SSLException e) {
                            logger.info(
                                "This engine was forced to close inbound, " +
                                    "without having received the proper SSL/TLS close " +
                                    "notification message from the peer, due to end of stream.",
                                e
                            );
                        }
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        break;
                    }
                    peerNetData.flip();
                    try {
                        result = engine.unwrap(peerNetData, peerAppData);
                        peerNetData.compact();
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException e) {
                        logger.info(
                            "A problem was encountered while processing the data " +
                                "that caused the SSLEngine to abort. " +
                                "Will try to properly close connection...",
                            e
                        );
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        break;
                    }
                    switch (result.getStatus()) {
                        case BUFFER_OVERFLOW:
                            peerAppData = enlargeBuffer(peerAppData, appBufferSize);
                            break;
                        case BUFFER_UNDERFLOW:
                            peerNetData = enlargeBufferIfNeed(peerNetData, netBufferSize);
                            break;
                        case CLOSED:
                            if (engine.isOutboundDone()) {
                                throw new SSLException("status CLOSED while outbound done");
                            } else {
                                engine.closeOutbound();
                                handshakeStatus = engine.getHandshakeStatus();
                                break;
                            }
                        default: // OK
                            break;
                    }
                    break;
                case NEED_WRAP:
                    netBuffer.clear();
                    try {
                        result = engine.wrap(appBuffer, netBuffer);
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException e) {
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        logger.info(
                            "A problem was encountered while processing the data " +
                                "that caused the SSLEngine to abort. " +
                                "Will try to properly close connection...",
                            e
                        );
                        break;
                    }
                    switch (result.getStatus()) {
                        case BUFFER_OVERFLOW:
                            netBuffer = enlargeBuffer(netBuffer, netBufferSize);
                            break;
                        case BUFFER_UNDERFLOW:
                            throw new SSLException("Buffer underflow occurred after a wrap.");
                        case CLOSED:
                            try {
                                writeOrTimeout(channel, netBuffer, endTime);
                                peerNetData.clear();
                            } catch (Exception e) {
                                logger.info(
                                    "Failed to send server's close message " +
                                        "due to socket channel's failure.",
                                    e
                                );
                                handshakeStatus = engine.getHandshakeStatus();
                            }
                            break;
                        default: // OK
                            writeOrTimeout(channel, netBuffer, endTime);
                            break;
                    }
                    break;
                default: // NEED_TASK:
                    Runnable task;
                    while ((task = engine.getDelegatedTask()) != null) {
                        task.run();
                    }
                    handshakeStatus = engine.getHandshakeStatus();
                    break;
            }
        }
        handshaked = true;
    }

    public byte[] read(ByteBuffer buffer) throws Exception {
        if (netBuffer.position() > 0) {
            netBuffer.compact();
        }
        netBuffer.put(buffer);
        netBuffer.flip();
        ByteBuffer tcpAppBuffer = ByteBuffer.allocate(appBufferSize);
        while (netBuffer.hasRemaining()) {
            SSLEngineResult result = engine.unwrap(netBuffer, tcpAppBuffer);
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    netBuffer = enlargeBuffer(netBuffer, appBufferSize);
                    break;
                case BUFFER_UNDERFLOW:
                    tcpAppBuffer = enlargeBufferIfNeed(tcpAppBuffer, netBufferSize);
                    break;
                case CLOSED:
                    safeCloseOutbound();
                    throw new EzyConnectionCloseException(
                        "ssl unwrap result status is CLOSE"
                    );
                default: // 0K
                    tcpAppBuffer.flip();
                    byte[] binary = new byte[tcpAppBuffer.limit()];
                    tcpAppBuffer.get(binary);
                    return binary;
            }
        }
        return new byte[0];
    }

    @Override
    public byte[] pack(byte[] bytes) throws Exception {
        if (!handshaked) {
            throw new SSLException("not handshaked");
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        ByteBuffer netBuffer = ByteBuffer.allocate(netBufferSize);
        while (buffer.hasRemaining()) {
            SSLEngineResult result = engine.wrap(
                buffer,
                netBuffer
            );
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    netBuffer = enlargeBuffer(netBuffer, netBufferSize);
                    break;
                case BUFFER_UNDERFLOW:
                    throw new IOException("Buffer underflow occurred after a wrap");
                case CLOSED:
                    safeCloseOutbound();
                    throw new EzyConnectionCloseException(
                        "ssl wrap result status is CLOSE"
                    );
                default: // OK
                    netBuffer.flip();
                    byte[] answer = new byte[netBuffer.limit()];
                    netBuffer.get(answer);
                    return answer;
            }
        }
        return bytes;
    }

    public static void writeOrTimeout(
        SocketChannel channel,
        ByteBuffer buffer,
        long timeoutAt
    ) throws IOException {
        buffer.flip();
        while (buffer.hasRemaining()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= timeoutAt) {
                throw new SSLException("Timeout");
            }
            channel.write(buffer);
        }
    }

    private void safeCloseOutbound() {
        try {
            engine.closeOutbound();
        } catch (Throwable e) {
            logger.info("close outbound error", e);
        }
    }

    private ByteBuffer enlargeBuffer(
        ByteBuffer buffer,
        int sessionProposedCapacity
    ) {
        return sessionProposedCapacity > buffer.capacity()
            ? ByteBuffer.allocate(sessionProposedCapacity)
            : ByteBuffer.allocate(buffer.capacity() * 2);
    }

    private ByteBuffer enlargeBufferIfNeed(
        ByteBuffer buffer,
        int packageBufferSize
    ) {
        if (packageBufferSize < buffer.limit()) {
            return buffer;
        } else {
            ByteBuffer replaceBuffer = enlargeBuffer(
                buffer,
                packageBufferSize
            );
            buffer.flip();
            replaceBuffer.put(buffer);
            return replaceBuffer;
        }
    }
}
