package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBuffer;
import static com.tvd12.ezyfoxserver.ssl.SslByteBuffers.enlargeBufferIfNeed;

public class SslByteBuffersTest {

    @Test
    public void enlargeBufferTest() {
        // given
        ByteBuffer buffer = ByteBuffer.allocate(1);
        int sessionProposedCapacity = RandomUtil.randomSmallInt() + 2;

        // when
        ByteBuffer actual = enlargeBuffer(
            buffer,
            sessionProposedCapacity
        );

        // then
        Asserts.assertEquals(actual.capacity(), sessionProposedCapacity);
    }

    @Test
    public void enlargeBufferIfNeedTest() {
        // given
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put(new byte[] {1, 2});
        buffer.flip();

        // when
        ByteBuffer actual = enlargeBufferIfNeed(
            buffer,
            1
        );

        // then
        Asserts.assertEquals(actual, buffer);
    }
}
