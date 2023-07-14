package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.nio.ByteBuffer;

import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBuffer;
import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBufferIfNeed;

public class EzySecureSocketDataReceiver extends EzySocketDataReceiver {

    protected final ByteBuffer[] tcpNetBuffers;

    public EzySecureSocketDataReceiver(Builder builder) {
        super(builder);
        this.tcpNetBuffers = newTcpByteBuffers(threadPoolSize);
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
        ByteBuffer appBuffer = buffer;
        int index = Math.abs(channel.hashCode() % threadPoolSize);
        ByteBuffer tcpNetBuffer = tcpNetBuffers[index];
        while (appBuffer.hasRemaining()) {
            tcpNetBuffer.clear();
            SSLEngineResult result = engine.unwrap(appBuffer, tcpNetBuffer);
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    appBuffer = enlargeBuffer(appBuffer, appBufferSize);
                    break;
                case BUFFER_UNDERFLOW:
                    tcpNetBuffer = enlargeBufferIfNeed(tcpNetBuffer, packageBufferSize);
                    break;
                case CLOSED:
                    engine.closeOutbound();
                    throw new EzyConnectionCloseException(
                        "ssl unwrap result status is CLOSE"
                    );
                default: // 0K
                    tcpNetBuffer.flip();
                    byte[] binary = new byte[tcpNetBuffer.limit()];
                    tcpNetBuffer.get(binary);
                    return binary;
            }
        }
        return new byte[0];
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
