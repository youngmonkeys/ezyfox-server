package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzySimplePacketTest {

    @Test
    public void test() {
        EzySimplePacket packet = new EzySimplePacket();
        assert packet.getSize() == 0;
        packet.setData("hello");
        packet.setFragment("hello");
        packet.setBinary(false);
        packet.setTransportType(EzyTransportType.TCP);
        assert packet.getData().equals("hello");
        assert packet.isFragmented();
        assert !packet.isBinary();
        assert packet.getTransportType() == EzyTransportType.TCP;
        assert packet.getSize() == "hello".length();
        System.out.println(packet);
    }

    @Test
    public void replaceDataTest() {
        // given
        EzySimplePacket packet = new EzySimplePacket();
        packet.setData("hello");

        // when
        packet.replaceData("world");

        // then
        Asserts.assertEquals(packet.getData(), "world");
    }
}
