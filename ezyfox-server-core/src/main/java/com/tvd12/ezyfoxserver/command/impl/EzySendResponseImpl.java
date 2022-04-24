package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyLoggerSetting;
import com.tvd12.ezyfoxserver.socket.EzySimplePackage;

import java.util.Collection;
import java.util.Set;

public class EzySendResponseImpl extends EzyAbstractCommand implements EzySendResponse {

    protected final EzyServer server;
    protected final EzyLoggerSetting loggerSetting;
    protected final Set<EzyConstant> ignoredLogCommands;

    public EzySendResponseImpl(EzyServer server) {
        this.server = server;
        this.loggerSetting = server.getSettings().getLogger();
        this.ignoredLogCommands =
            loggerSetting.getIgnoredCommands().getCommands();
    }

    @Override
    public void execute(
        EzyResponse response,
        EzySession recipient,
        boolean encrypted,
        boolean immediate,
        EzyTransportType transportType
    ) {
        boolean success = false;
        EzyResponseApi responseApi = server.getResponseApi();
        EzyArray data = response.serialize();
        EzySimplePackage pack = newPackage(data, encrypted, transportType);
        pack.addRecipient(recipient);
        try {
            responseApi.response(pack, immediate);
            success = true;
        } catch (Exception e) {
            logger.error(
                "send data: {}, to client: {} error",
                pack.getData(),
                recipient.getName(),
                e
            );
        } finally {
            pack.release();
        }
        boolean debug = server.getSettings().isDebug();
        if (debug
            && success
            && !ignoredLogCommands.contains(response.getCommand())
        ) {
            logger.debug("send to: {} data: {}", recipient.getName(), data);
        }
    }

    @Override
    public void execute(
        EzyResponse response,
        Collection<EzySession> recipients,
        boolean encrypted,
        boolean immediate,
        EzyTransportType transportType
    ) {
        boolean success = false;
        EzyResponseApi responseApi = server.getResponseApi();
        EzyArray data = response.serialize();
        EzySimplePackage pack = newPackage(data, encrypted, transportType);
        pack.addRecipients(recipients);
        try {
            responseApi.response(pack, immediate);
            success = true;
        } catch (Exception e) {
            logger.error(
                "send data: {}, to client: {} error",
                pack.getData(),
                getRecipientsNames(recipients),
                e
            );
        } finally {
            pack.release();
        }
        boolean debug = server.getSettings().isDebug();
        if (debug
            && success
            && !ignoredLogCommands.contains(response.getCommand())
        ) {
            logger.debug(
                "send to: {} data: {}",
                getRecipientsNames(recipients),
                data
            );
        }
    }

    protected EzySimplePackage newPackage(
        EzyArray data,
        boolean encrypted,
        EzyTransportType transportType
    ) {
        EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(encrypted);
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
