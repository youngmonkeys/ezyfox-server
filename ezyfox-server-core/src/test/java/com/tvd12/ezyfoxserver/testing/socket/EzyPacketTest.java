package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyPacketTest extends BaseTest {

    @Test
    public void test() {
        EzyPacket packet = new EzySimplePacket();
        packet.setFragment(new byte[]{1, 2, 3});
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            packet.getSize();
        }
        long offset = System.currentTimeMillis() - start;
        System.out.println(offset);
    }

}
