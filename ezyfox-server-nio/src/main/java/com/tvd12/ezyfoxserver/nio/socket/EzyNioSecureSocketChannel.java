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
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;

public class EzyNioSecureSocketChannel
    extends EzyNioSocketChannel
    implements EzySecureChannel {

    private SSLEngine engine;
    private ByteBuffer netBuffer;
    private int appBufferSize;
    private int netBufferSize;
    private final SSLContext sslContext;
    private final int sslHandshakeTimeout;
    private final int sslMaxAppBufferSize;
    @Getter
    private final Object packingLock = new Object();
    private final AtomicBoolean handshaked = new AtomicBoolean();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);

    public EzyNioSecureSocketChannel(
        SocketChannel channel,
        SSLContext sslContext,
        int sslHandshakeTimeout,
        int maxRequestSize
    ) {
        super(channel);
        this.sslContext = sslContext;
        this.sslHandshakeTimeout = sslHandshakeTimeout;
        this.sslMaxAppBufferSize = maxRequestSize * 2;
    }

    public boolean isHandshaked() {
        return handshaked.get();
    }

    @SuppressWarnings("MethodLength")
    public void handshake() throws IOException {
        if (!channel.isConnected()) {
            logger.info("channel: {} closed", channel);
            return;
        }
        if (engine != null) {
            logger.info(
                "channel: {} has already called handshake, handshaked: {}",
                channel,
                handshaked
            );
            return;
        }
        engine = sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        engine.beginHandshake();

        SSLSession session = engine.getSession();
        appBufferSize = session.getApplicationBufferSize();
        netBufferSize = session.getPacketBufferSize();

        netBuffer = ByteBuffer.allocate(netBufferSize);
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
                                "this engine was forced to close inbound, " +
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
                            "a problem was encountered while processing the data " +
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
                            throw new EzyConnectionCloseException("max request size");
                        case BUFFER_UNDERFLOW:
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
                        result = engine.wrap(EMPTY_BUFFER, netBuffer);
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException e) {
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        logger.info(
                            "a problem was encountered while processing the data " +
                                "that caused the SSLEngine to abort. " +
                                "Will try to properly close connection...",
                            e
                        );
                        break;
                    }
                    switch (result.getStatus()) {
                        case BUFFER_OVERFLOW:
                            netBuffer = ByteBuffer.allocate(netBuffer.capacity() * 2);
                            break;
                        case BUFFER_UNDERFLOW:
                            throw new SSLException(
                                "should not happen, buffer underflow occurred after a wrap."
                            );
                        case CLOSED:
                            try {
                                writeOrTimeout(channel, netBuffer, endTime);
                                peerNetData.clear();
                            } catch (Exception e) {
                                logger.info(
                                    "failed to send server's close message " +
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
        handshaked.set(true);
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
                    int doubleSize = tcpAppBuffer.capacity() * 2;
                    if (doubleSize > sslMaxAppBufferSize) {
                        throw new EzyConnectionCloseException("max request size");
                    }
                    tcpAppBuffer = ByteBuffer.allocate(doubleSize);
                    break;
                case BUFFER_UNDERFLOW:
                    break;
                case CLOSED:
                    try {
                        engine.closeOutbound();
                    } catch (Throwable e) {
                        throw new EzyConnectionCloseException(
                            "ssl unwrap result status is CLOSE",
                            e
                        );
                    }
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
        if (!handshaked.get()) {
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
                    netBuffer = ByteBuffer.allocate(netBuffer.capacity() * 2);
                    break;
                case BUFFER_UNDERFLOW:
                    throw new IOException(
                        "should not happen, buffer underflow occurred after a wrap."
                    );
                case CLOSED:
                    try {
                        engine.closeOutbound();
                    } catch (Throwable e) {
                        throw new EzyConnectionCloseException(
                            "ssl wrap result status is CLOSE",
                            e
                        );
                    }
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

    public void writeOrTimeout(
        SocketChannel channel,
        ByteBuffer buffer,
        long timeoutAt
    ) throws IOException {
        buffer.flip();
        while (buffer.hasRemaining()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= timeoutAt) {
                break;
            }
            channel.write(buffer);
        }
    }

    @Override
    public void close() {
        super.close();
        if (engine != null) {
            processWithLogException(engine::closeOutbound);
        }
    }
}
