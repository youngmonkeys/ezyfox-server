package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySecureChannel;

public class EzySecureSocketResponseApi extends EzySocketResponseApi {

    public EzySecureSocketResponseApi(Object encoder) {
        super(encoder);
    }

    @Override
    protected void sendTcpPacket(
        EzySession session,
        EzyPacket packet
    ) throws Exception {
        EzyChannel channel = session.getChannel();
        if (channel == null) {
            session.send(packet);
        }
        EzySecureChannel secureChannel = (EzySecureChannel) channel;
        try {
            synchronized (secureChannel.getPackLock()) {
                byte[] packedBytes = secureChannel.pack(
                    (byte[]) packet.getData()
                );
                packet.replaceData(packedBytes);
                session.send(packet);
            }
        } catch (EzyConnectionCloseException e) {
            session.disconnect();
            throw e;
        }
    }

    @Override
    protected void sendTcpPacketNow(
        EzySession session,
        EzyPacket packet
    ) throws Exception {
        EzyChannel channel = session.getChannel();
        if (channel == null) {
            session.sendNow(packet);
        }
        EzySecureChannel secureChannel = (EzySecureChannel) channel;
        try {
            synchronized (secureChannel.getPackLock()) {
                byte[] packedBytes = secureChannel.pack(
                    (byte[]) packet.getData()
                );
                packet.replaceData(packedBytes);
                session.sendNow(packet);
            }
        } catch (EzyConnectionCloseException e) {
            session.disconnect();
            throw e;
        }
    }
}
