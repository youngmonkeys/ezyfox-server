package com.tvd12.ezyfoxserver.socket;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

import com.tvd12.ezyfox.util.EzyReleasable;

public interface EzyUdpReceivedPacket extends EzyReleasable {

    DatagramChannel getChannel();
    
    InetSocketAddress getAddress();
    
    byte[] getBytes();
    
}
