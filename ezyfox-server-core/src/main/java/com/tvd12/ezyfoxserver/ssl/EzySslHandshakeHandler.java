package com.tvd12.ezyfoxserver.ssl;

import com.tvd12.ezyfox.util.EzyLoggable;
import lombok.AllArgsConstructor;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.tvd12.ezyfoxserver.socket.EzySocketChannels.write;
import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBuffer;
import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBufferIfNeed;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;

@AllArgsConstructor
public class EzySslHandshakeHandler extends EzyLoggable {

    private final SSLContext sslContext;
    private final int timeout;

    @SuppressWarnings("MethodLength")
    public SSLEngine handle(
        SocketChannel socketChannel
    ) throws IOException {
        SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        engine.beginHandshake();

        SSLSession session = engine.getSession();
        int appBufferSize = session.getApplicationBufferSize();
        int packetBufferSize = session.getPacketBufferSize();
        ByteBuffer appBuffer = ByteBuffer.allocate(appBufferSize);
        ByteBuffer netBuffer = ByteBuffer.allocate(packetBufferSize);
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerNetData = ByteBuffer.allocate(packetBufferSize);

        SSLEngineResult result;
        SSLEngineResult.HandshakeStatus handshakeStatus = engine.getHandshakeStatus();
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + timeout;
        while (handshakeStatus != FINISHED && handshakeStatus != NOT_HANDSHAKING) {
            currentTime = System.currentTimeMillis();
            if (currentTime >= endTime) {
                throw new SSLException("Timeout");
            }
            switch (handshakeStatus) {
                case NEED_UNWRAP:
                    int readBytes = socketChannel.read(peerNetData);
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
                            peerNetData = enlargeBufferIfNeed(peerNetData, packetBufferSize);
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
                            netBuffer = enlargeBuffer(netBuffer, packetBufferSize);
                            break;
                        case BUFFER_UNDERFLOW:
                            throw new SSLException("Buffer underflow occurred after a wrap.");
                        case CLOSED:
                            try {
                                write(socketChannel, netBuffer, endTime);
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
                            write(socketChannel, netBuffer, endTime);
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
        if (handshakeStatus == NOT_HANDSHAKING) {
            throw new SSLException("not handshaking");
        }
        return engine;
    }
}
