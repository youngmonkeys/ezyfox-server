package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeApplicationBuffer;
import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargePacketBuffer;

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
        ByteBuffer appBuffer = buffer;
        int index = Math.abs(channel.hashCode() % threadPoolSize);
        ByteBuffer tcpNetBuffer = tcpNetBuffers[index];
        while (appBuffer.hasRemaining()) {
            tcpNetBuffer.clear();
            SSLEngineResult result = engine.unwrap(appBuffer, tcpNetBuffer);
            switch (result.getStatus()) {
                case OK:
                    tcpNetBuffer.flip();
                    byte[] binary = new byte[tcpNetBuffer.limit()];
                    tcpNetBuffer.get(binary);
                    return binary;
                case BUFFER_OVERFLOW:
                    appBuffer = enlargeApplicationBuffer(engine, appBuffer);
                    break;
                case BUFFER_UNDERFLOW:
                    tcpNetBuffer = handleBufferUnderflow(engine, tcpNetBuffer);
                    break;
                case CLOSED:
                    engine.closeOutbound();
                    throw new EzyConnectionCloseException(
                        "ssl unwrap result status is CLOSE"
                    );
                default:
                    throw new IOException(
                        "Invalid SSL status: " + result.getStatus()
                    );
            }
        }
        return new byte[0];
    }

    protected ByteBuffer handleBufferUnderflow(
        SSLEngine engine,
        ByteBuffer buffer) {
        if (engine.getSession().getPacketBufferSize() < buffer.limit()) {
            return buffer;
        } else {
            ByteBuffer replaceBuffer = enlargePacketBuffer(engine, buffer);
            buffer.flip();
            replaceBuffer.put(buffer);
            return replaceBuffer;
        }
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
