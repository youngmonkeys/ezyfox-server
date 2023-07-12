package com.tvd12.ezyfoxserver.nio.socket;

import lombok.Getter;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargePacketBuffer;

public class EzyNioSecureSocketChannel extends EzyNioSocketChannel {

    private ByteBuffer netBuffer;
    @Getter
    private final SSLEngine engine;

    public EzyNioSecureSocketChannel(
        SocketChannel channel,
        SSLEngine engine
    ) {
        super(channel);
        this.engine = engine;
        SSLSession session = engine.getSession();
        int packetBufferSize = session.getPacketBufferSize();
        this.netBuffer = ByteBuffer.allocate(packetBufferSize);
    }

    @Override
    public int write(Object data, boolean binary) throws Exception {
        ByteBuffer dataToWrite = (ByteBuffer) data;
        netBuffer.clear();
        SSLEngineResult result = engine.wrap(dataToWrite, netBuffer);
        switch (result.getStatus()) {
            case OK:
                netBuffer.flip();
                return channel.write(netBuffer);
            case BUFFER_OVERFLOW:
                netBuffer = enlargePacketBuffer(engine, netBuffer);
                return 0;
            case BUFFER_UNDERFLOW:
                throw new IOException("Buffer underflow occurred after a wrap");
            case CLOSED:
                engine.closeOutbound();
                channel.close();
                return -1;
            default:
                throw new IOException("Invalid SSL status: " + result.getStatus());
        }
    }
}
