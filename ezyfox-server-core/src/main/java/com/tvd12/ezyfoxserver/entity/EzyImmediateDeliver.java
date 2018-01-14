package com.tvd12.ezyfoxserver.entity;

import com.tvd12.ezyfoxserver.socket.EzyPacket;

public interface EzyImmediateDeliver {

    void sendPacketNow(EzyPacket packet);
    
}
