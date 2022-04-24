package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.util.EzyReleasable;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public interface EzyUdpReceivedPacket extends EzyReleasable {

    DatagramChannel getChannel();

    InetSocketAddress getAddress();

    byte[] getBytes();

}
