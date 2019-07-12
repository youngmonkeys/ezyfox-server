package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzyNonBlockingPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;
import com.tvd12.test.base.BaseTest;

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
        for(int i = 0 ; i < 256 ; ++i) {
            queue.add(new Packet());
        }
        assert !queue.add(new Packet());
    }
    
    public static class Packet extends EzySimplePacket {
    }
    
    
}
