package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyConnectionTypes;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolChannel;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;

import java.util.Collection;

public class EzyProtocolResponseApi
    extends EzyLoggable
    implements EzyResponseApi {

    @Override
    public void response(
        EzyPackage pack,
        boolean immediate
    ) {
        for (EzyConstant connectionType : pack.getConnectionTypes()) {
            if (connectionType == null || EzyConnectionTypes.isBuiltIn(connectionType)) {
                continue;
            }
            response(pack, connectionType, immediate);
        }
    }

    private void response(
        EzyPackage pack,
        EzyConstant connectionType,
        boolean immediate
    ) {
        Collection<EzySession> recipients = pack.getRecipients(connectionType);
        EzyArray data = pack.getData();
        for (EzySession session : recipients) {
            EzySimplePacket packet = null;
            try {
                EzyChannel channel = session.getChannel();
                if (!(channel instanceof EzyProtocolChannel)) {
                    continue;
                }
                EzyProtocolChannel protocolChannel =
                    (EzyProtocolChannel) channel;
                Object bytes = protocolChannel.encode(data);
                if (bytes == null) {
                    continue;
                }
                packet = new EzySimplePacket();
                packet.setData(bytes);
                packet.setBinary(protocolChannel.isBinary());
                if (immediate) {
                    session.sendNow(packet);
                } else {
                    session.send(packet);
                }
            } catch (Throwable e) {
                if (packet != null) {
                    packet.release();
                }
                logger.info(
                    "response data to {} session: {} failed",
                    connectionType.getName(),
                    session,
                    e
                );
            }
        }
    }
}
