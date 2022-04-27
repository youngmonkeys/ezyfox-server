package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfoxserver.socket.EzyUdpReceivedPacket;

public interface EzyNioUdpDataHandler {

    void fireUdpPacketReceived(EzyUdpReceivedPacket packet) throws Exception;
}
