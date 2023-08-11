package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
            super.tcpReadBytes(channel, buffer);
        }
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzySocketDataReceiver.Builder {

        @Override
        public EzySocketDataReceiver build() {
            return new EzySecureSocketDataReceiver(this);
        }
    }
}
