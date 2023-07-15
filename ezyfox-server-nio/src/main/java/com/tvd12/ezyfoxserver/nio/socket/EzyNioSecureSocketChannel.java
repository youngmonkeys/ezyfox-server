package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import lombok.Getter;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.tvd12.ezyfoxserver.ssl.EzySslEngines.safeCloseOutbound;
import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBuffer;

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
        SSLSession session = engine.getSession();
        int netBufferLength = session.getPacketBufferSize();
        ByteBuffer netBuffer = ByteBuffer.allocate(netBufferLength);
        while (buffer.hasRemaining()) {
            SSLEngineResult result = engine.wrap(
                buffer,
                netBuffer
            );
            switch (result.getStatus()) {
                case BUFFER_OVERFLOW:
                    netBuffer = enlargeBuffer(netBuffer, netBufferLength);
                    break;
                case BUFFER_UNDERFLOW:
                    throw new IOException("Buffer underflow occurred after a wrap");
                case CLOSED:
                    safeCloseOutbound(engine);
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
}
