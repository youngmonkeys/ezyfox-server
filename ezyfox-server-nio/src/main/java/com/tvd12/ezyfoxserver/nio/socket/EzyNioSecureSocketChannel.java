package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import lombok.Getter;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargePacketBuffer;

public class EzyNioSecureSocketChannel extends EzyNioSocketChannel {

    @Getter
    private final SSLEngine engine;

    public EzyNioSecureSocketChannel(
        SocketChannel channel,
        SSLEngine engine
    ) {
        super(channel);
        this.engine = engine;
    }

    @Override
    public byte[] pack(byte[] bytes) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int netBufferLength = engine
            .getSession()
            .getPacketBufferSize();
        ByteBuffer netBuffer = ByteBuffer.allocate(netBufferLength);
        while (buffer.hasRemaining()) {
            SSLEngineResult result = engine.wrap(
                buffer,
                netBuffer
            );
            switch (result.getStatus()) {
                case OK:
                    netBuffer.flip();
                    byte[] answer = new byte[netBuffer.limit()];
                    netBuffer.get(answer);
                    return answer;
                case BUFFER_OVERFLOW:
                    netBuffer = enlargePacketBuffer(engine, netBuffer);
                    break;
                case BUFFER_UNDERFLOW:
                    throw new IOException("Buffer underflow occurred after a wrap");
                case CLOSED:
                    engine.closeOutbound();
                    throw new EzyConnectionCloseException(
                        "ssl wrap result status is CLOSE"
                    );
                default:
                    throw new IOException("Invalid SSL status: " + result.getStatus());
            }
        }
        return bytes;
    }
}
