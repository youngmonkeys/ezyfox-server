package com.tvd12.ezyfoxserver.ssl;

import com.tvd12.ezyfox.util.EzyLoggable;
import lombok.AllArgsConstructor;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
        SSLException sslException = null;
        SSLEngineResult.HandshakeStatus handshakeStatus = engine.getHandshakeStatus();
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + timeout;
        while (handshakeStatus != FINISHED && handshakeStatus != NOT_HANDSHAKING) {
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
                                    "notification message from the peer, due to end of stream."
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
                                "Will try to properly close connection..."
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
                        sslException = e;
                        logger.info(
                            "A problem was encountered while processing the data " +
                                "that caused the SSLEngine to abort. " +
                                "Will try to properly close connection..."
                        );
                        break;
                    }
                    switch (result.getStatus()) {
                        case OK :
                            netBuffer.flip();
                            while (netBuffer.hasRemaining()) {
                                int writeBytes = socketChannel.write(netBuffer);
                                if (writeBytes < 0) {
                                    throw new SSLException("Maybe client closed.");
                                }
                            }
                            break;
                        case BUFFER_OVERFLOW:
                            netBuffer = enlargeBuffer(netBuffer, packetBufferSize);
                            break;
                        case BUFFER_UNDERFLOW:
                            throw new SSLException("Buffer underflow occurred after a wrap.");
                        case CLOSED:
                            try {
                                netBuffer.flip();
                                while (netBuffer.hasRemaining()) {
                                    int writeBytes = socketChannel.write(netBuffer);
                                    if (writeBytes < 0) {
                                        throw new SSLException("Maybe client closed.");
                                    }
                                }
                                peerNetData.clear();
                            } catch (Exception e) {
                                logger.info(
                                    "Failed to send server's close message " +
                                        "due to socket channel's failure."
                                );
                                handshakeStatus = engine.getHandshakeStatus();
                            }
                            break;
                        default:
                            throw new SSLException(
                                "Invalid SSL status: " + result.getStatus()
                            );
                    }
                    break;
                case NEED_TASK:
                    Runnable task;
                    while ((task = engine.getDelegatedTask()) != null) {
                        task.run();
                    }
                    handshakeStatus = engine.getHandshakeStatus();
                    break;
                default:
                    throw new SSLException("Invalid SSL status: " + handshakeStatus);
            }
            currentTime = System.currentTimeMillis();
            if (currentTime >= endTime) {
                throw new SSLException("Timeout");
            }
        }
        if (sslException != null) {
            throw sslException;
        }
        return engine;
    }
}
