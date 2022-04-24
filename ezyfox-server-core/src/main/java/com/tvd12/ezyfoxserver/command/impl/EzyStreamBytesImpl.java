package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyStreamBytes;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzySimpleBytesPackage;

import java.util.Collection;

public class EzyStreamBytesImpl extends EzyAbstractCommand implements EzyStreamBytes {

    protected final EzyServer server;

    public EzyStreamBytesImpl(EzyServer server) {
        this.server = server;
    }

    @Override
    public void execute(
        byte[] bytes,
        EzySession recipient,
        EzyTransportType transportType
    ) {
        EzyStreamingApi streamingApi = server.getStreamingApi();
        EzySimpleBytesPackage pack = newPackage(bytes, transportType);
        pack.addRecipient(recipient);
        try {
            streamingApi.response(pack);
        } catch (Exception e) {
            logger.warn("send {} bytes {}, to client: {} error", bytes.length, recipient.getName(), e);
        } finally {
            pack.release();
        }
    }

    @Override
    public void execute(
        byte[] bytes,
        Collection<EzySession> recipients,
        EzyTransportType transportType
    ) {
        EzyStreamingApi streamingApi = server.getStreamingApi();
        EzySimpleBytesPackage pack = newPackage(bytes, transportType);
        pack.addRecipients(recipients);
        try {
            streamingApi.response(pack);
        } catch (Exception e) {
            logger.warn("send: {} bytes, to client: {} error", bytes.length, getRecipientsNames(recipients), e);
        } finally {
            pack.release();
        }
    }

    protected EzySimpleBytesPackage newPackage(
        byte[] bytes,
        EzyTransportType transportType
    ) {
        EzySimpleBytesPackage pack = new EzySimpleBytesPackage();
        pack.setBytes(bytes);
        pack.setTransportType(transportType);
        return pack;
    }

    protected String getRecipientsNames(
        Collection<EzySession> recipients
    ) {
        StringBuilder builder = new StringBuilder()
            .append("[ ");
        for (EzySession receiver : recipients) {
            builder.append(receiver.getName()).append(" ");
        }
        return builder.append("]").toString();
    }
}
