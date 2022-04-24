package com.tvd12.ezyfoxserver.entity;

import com.tvd12.ezyfoxserver.socket.EzyPacket;

public interface EzyDeliver {

    void send(EzyPacket packet);

    void sendNow(EzyPacket packet);

}
