package com.tvd12.ezyfoxserver.ssl;

import javax.net.ssl.SSLEngine;
import java.nio.ByteBuffer;

public final class SslByteBuffers {

    private SslByteBuffers() {}

    public static ByteBuffer enlargePacketBuffer(
        SSLEngine engine,
        ByteBuffer buffer
    ) {
        return enlargeBuffer(
            buffer,
            engine.getSession().getPacketBufferSize()
        );
    }

    public static ByteBuffer enlargeApplicationBuffer(
        SSLEngine engine,
        ByteBuffer buffer
    ) {
        return enlargeBuffer(
            buffer,
            engine.getSession().getApplicationBufferSize()
        );
    }

    public static ByteBuffer enlargeBuffer(
        ByteBuffer buffer,
        int sessionProposedCapacity
    ) {
        return sessionProposedCapacity > buffer.capacity()
            ? ByteBuffer.allocate(sessionProposedCapacity)
            : ByteBuffer.allocate(buffer.capacity() * 2);
    }
}
