package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeHandler;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.tvd12.ezyfoxserver.constant.EzyCoreConstants.MAX_SECURE_READ_BUFFER_SIZE;
import static com.tvd12.ezyfoxserver.ssl.EzySslEngines.safeCloseOutbound;
import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBuffer;
import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBufferIfNeed;

public class EzySecureSocketDataReceiver extends EzySocketDataReceiver {

    protected final EzySslHandshakeHandler sslHandshakeHandler;

    public EzySecureSocketDataReceiver(Builder builder) {
        super(builder);
        this.sslHandshakeHandler = builder.sslHandshakeHandler;
    }

    @Override
    protected void tcpReadBytes(
        SocketChannel channel,
        ByteBuffer buffer
    ) throws Throwable {
        EzyNioHandlerGroup handlerGroup =
            handlerGroupManager.getHandlerGroup(channel);
        if (handlerGroup != null) {
            EzyNioSecureSocketChannel secureChannel =
                (EzyNioSecureSocketChannel) handlerGroup.getChannel();
            if (secureChannel.getEngine() == null) {
                try {
                    secureChannel.setEngine(
                        sslHandshakeHandler.handle(channel)
                    );
                } catch (Exception e) {
                    logger.info("handshake failed on channel: {}", channel, e);
                    handlerGroup.enqueueDisconnection(
                        EzyDisconnectReason.SSH_HANDSHAKE_FAILED
                    );
                    return;
                }
            }
        }
        super.tcpReadBytes(channel, buffer);
    }

    @Override
    protected byte[] readTcpBytesFromBuffer(
        EzyChannel channel,
        ByteBuffer buffer
    ) throws Exception {
        EzyNioSecureSocketChannel secureChannel =
            (EzyNioSecureSocketChannel) channel;
        SSLEngine engine = secureChannel.getEngine();
        SSLSession session = engine.getSession();
        int appBufferSize = session.getApplicationBufferSize();
        int packageBufferSize = session.getPacketBufferSize();
        ByteBuffer netBuffer = secureChannel.getReadAppBuffer();
        if (netBuffer.position() > 0) {
            netBuffer.compact();
        }
        netBuffer.put(buffer);
        netBuffer.flip();
        ByteBuffer tcpAppBuffer = ByteBuffer.allocate(appBufferSize);
        while (netBuffer.hasRemaining()) {
            SSLEngineResult result;
            try {
                /*
                logger.info(
                    "before read on channel: {}, netbuffer: {}, appBuffer: {}",
                    channel,
                    netBuffer,
                    tcpAppBuffer
                );*/
                result = engine.unwrap(netBuffer, tcpAppBuffer);
            } catch (Exception e) {
                /*
                logger.info(
                    "after read on channel: {} error, netbuffer: {}, appBuffer: {}",
                    channel,
                    netBuffer,
                    tcpAppBuffer,
                    e
                );*/
                throw e;
            }
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    netBuffer = enlargeBuffer(netBuffer, appBufferSize);
                    break;
                case BUFFER_UNDERFLOW:
                    tcpAppBuffer = enlargeBufferIfNeed(tcpAppBuffer, packageBufferSize);
                    break;
                case CLOSED:
                    safeCloseOutbound(engine);
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
    protected int getMaxBufferSize() {
        return MAX_SECURE_READ_BUFFER_SIZE;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzySocketDataReceiver.Builder {

        protected EzySslHandshakeHandler sslHandshakeHandler;

        public Builder sslHandshakeHandler(EzySslHandshakeHandler sslHandshakeHandler) {
            this.sslHandshakeHandler = sslHandshakeHandler;
            return this;
        }

        @Override
        public EzySocketDataReceiver build() {
            return new EzySecureSocketDataReceiver(this);
        }
    }
}
