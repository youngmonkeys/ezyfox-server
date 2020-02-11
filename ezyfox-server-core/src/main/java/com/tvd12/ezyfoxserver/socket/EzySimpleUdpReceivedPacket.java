package com.tvd12.ezyfoxserver.socket;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EzySimpleUdpReceivedPacket implements EzyUdpReceivedPacket {

    protected DatagramChannel channel;
    protected InetSocketAddress address;
    protected byte[] bytes;
    
    @Override
    public void release() {
        this.channel = null;
        this.address = null;
        this.bytes = null;
    }
    
}
