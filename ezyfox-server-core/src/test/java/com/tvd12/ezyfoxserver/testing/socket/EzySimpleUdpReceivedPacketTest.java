package com.tvd12.ezyfoxserver.testing.socket;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySimpleUdpReceivedPacket;

public class EzySimpleUdpReceivedPacketTest {

    @Test
    public void test() {
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        InetSocketAddress address = new InetSocketAddress(12345);
        byte[] bytes = new byte[] {1, 2, 3};
        EzySimpleUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
                channel, address, bytes);
        assert packet.getAddress() != null;
        assert packet.getBytes() != null;
        System.out.println(packet.getChannel());
        packet.release();
    }
    
}
