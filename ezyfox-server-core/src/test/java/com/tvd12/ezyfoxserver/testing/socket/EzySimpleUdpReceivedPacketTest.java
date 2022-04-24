package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySimpleUdpReceivedPacket;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class EzySimpleUdpReceivedPacketTest {

    @Test
    public void test() {
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        InetSocketAddress address = new InetSocketAddress(12345);
        byte[] bytes = new byte[]{1, 2, 3};
        EzySimpleUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
            channel, address, bytes);
        assert packet.getAddress() != null;
        assert packet.getBytes() != null;
        System.out.println(packet.getChannel());
        packet.release();
    }

}
