package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzyNonBlockingPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzyNonBlockingRequestQueue;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;
import com.tvd12.ezyfoxserver.socket.EzySocketRequest;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class EzyNonBlockingPacketQueueTest extends BaseTest {

    @Test
    public void test() {
        EzyNonBlockingPacketQueue queue = new EzyNonBlockingPacketQueue();
        assert queue.size() == 0;
        queue.add(new Packet());
        queue.add(new Packet());
        assert queue.size() == 2;
        queue.take();
        queue.take();
        assert queue.isEmpty();
    }

    @Test
    public void test2() {
        EzyNonBlockingPacketQueue queue = new EzyNonBlockingPacketQueue();
        for (int i = 0; i < 256; ++i) {
            queue.add(new Packet());
        }
        assert !queue.add(new Packet());
    }

    @Test
    public void clearTest() {
        // given
        EzyNonBlockingRequestQueue sut = new EzyNonBlockingRequestQueue();
        EzySocketRequest socketRequest = mock(EzySocketRequest.class);
        sut.add(socketRequest);

        // when
        sut.clear();

        // then
        Asserts.assertTrue(sut.isEmpty());
    }

    @Test
    public void isFullTest() {
        // given
        EzyNonBlockingRequestQueue sut = new EzyNonBlockingRequestQueue(0);
        EzySocketRequest socketRequest = mock(EzySocketRequest.class);

        // when
        boolean result = sut.add(socketRequest);

        // then
        Asserts.assertTrue(sut.isFull());
        Asserts.assertFalse(result);
    }

    public static class Packet extends EzySimplePacket {
    }
}
