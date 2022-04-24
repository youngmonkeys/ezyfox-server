package com.tvd12.ezyfoxserver.socket;

public interface EzySocketWriterGroup {

    void firePacketSend(EzyPacket packet, Object writeBuffer) throws Exception;

}
