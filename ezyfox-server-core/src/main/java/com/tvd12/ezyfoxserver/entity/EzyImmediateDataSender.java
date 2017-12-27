package com.tvd12.ezyfoxserver.entity;

import com.tvd12.ezyfoxserver.socket.EzyPacket;

public interface EzyImmediateDataSender {

    void sendPacketNow(EzyPacket packet);
    
}
