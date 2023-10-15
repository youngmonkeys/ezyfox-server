package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySecureChannel;

import java.io.IOException;

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
            throw new IOException("session destroyed");
        }
        EzySecureChannel secureChannel = (EzySecureChannel) channel;
        try {
            synchronized (secureChannel.getPackingLock()) {
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
            throw new IOException("session destroyed");
        }
        EzySecureChannel secureChannel = (EzySecureChannel) channel;
        try {
            synchronized (secureChannel.getPackingLock()) {
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
