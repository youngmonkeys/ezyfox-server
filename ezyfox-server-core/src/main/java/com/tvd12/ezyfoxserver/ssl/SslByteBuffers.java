package com.tvd12.ezyfoxserver.ssl;

import java.nio.ByteBuffer;

public final class SslByteBuffers {

    private SslByteBuffers() {}

    public static ByteBuffer enlargeBuffer(
        ByteBuffer buffer,
        int sessionProposedCapacity
    ) {
        return sessionProposedCapacity > buffer.capacity()
            ? ByteBuffer.allocate(sessionProposedCapacity)
            : ByteBuffer.allocate(buffer.capacity() * 2);
    }

    public static ByteBuffer enlargeBufferIfNeed(
        ByteBuffer buffer,
        int packageBufferSize
    ) {
        if (packageBufferSize < buffer.limit()) {
            return buffer;
        } else {
            ByteBuffer replaceBuffer = enlargeBuffer(
                buffer,
                packageBufferSize
            );
            buffer.flip();
            replaceBuffer.put(buffer);
            return replaceBuffer;
        }
    }
}
