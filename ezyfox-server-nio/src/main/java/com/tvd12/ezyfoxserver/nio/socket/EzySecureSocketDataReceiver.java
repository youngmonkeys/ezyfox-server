package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeHandler;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.tvd12.ezyfoxserver.constant.EzyCoreConstants.MAX_SECURE_READ_BUFFER_SIZE;

public class EzySecureSocketDataReceiver extends EzySocketDataReceiver {

    public EzySecureSocketDataReceiver(Builder builder) {
        super(builder);
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
            if (!secureChannel.isHandshaked()) {
                try {
                    secureChannel.handshake();
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
        return secureChannel.read(buffer);
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
