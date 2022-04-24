package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyBytesPackage;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;

import java.util.Collection;

public abstract class EzyAbstractStreamingApi implements EzyStreamingApi {

    @Override
    public void response(EzyBytesPackage pack) throws Exception {
        EzyConstant connectionType = getConnectionType();
        Collection<EzySession> recipients = pack.getRecipients(connectionType);
        if (recipients.isEmpty()) {
            return;
        }
        byte[] bytes = pack.getBytes();
        for (EzySession session : recipients) {
            session.send(createPacket(bytes, pack));
        }
    }

    private EzyPacket createPacket(byte[] bytes, EzyBytesPackage pack) {
        EzySimplePacket packet = new EzySimplePacket();
        packet.setTransportType(pack.getTransportType());
        packet.setData(bytes);
        return packet;
    }

    protected abstract EzyConstant getConnectionType();
}
