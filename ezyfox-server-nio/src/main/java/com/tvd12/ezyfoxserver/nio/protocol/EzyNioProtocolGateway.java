package com.tvd12.ezyfoxserver.nio.protocol;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionTypes;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolChannel;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolGateway;

public class EzyNioProtocolGateway
    extends EzyLoggable
    implements EzyProtocolGateway {

    private final EzyHandlerGroupManager handlerGroupManager;

    public EzyNioProtocolGateway(
        EzyHandlerGroupManager handlerGroupManager
    ) {
        this.handlerGroupManager = handlerGroupManager;
    }

    @Override
    public EzySession openSession(EzyProtocolChannel channel) {
        String connectionType = channel.getConnectionType();
        if (EzyConnectionTypes.isBuiltIn(connectionType)) {
            throw new IllegalArgumentException(
                "protocol channel can not use built-in connection type: " +
                    connectionType
            );
        }
        EzyProtocolHandlerGroup handlerGroup = handlerGroupManager
            .newHandlerGroup(channel, connectionType);
        return handlerGroup.getSession();
    }

    @Override
    public EzySession getSession(Object connection) {
        EzyProtocolHandlerGroup handlerGroup = handlerGroupManager
            .getHandlerGroup(connection);
        return handlerGroup != null ? handlerGroup.getSession() : null;
    }

    @Override
    public void receive(
        EzySession session,
        EzyArray data,
        int dataSize
    ) {
        EzyProtocolHandlerGroup handlerGroup = getHandlerGroup(session);
        if (handlerGroup == null) {
            logger.debug(
                "has no handler group with session: {}, drop data: {}",
                session,
                data
            );
            return;
        }
        handlerGroup.fireDataReceived(data, dataSize);
    }

    @Override
    public void receive(
        EzySession session,
        EzyCommand command,
        EzyArray params,
        int dataSize
    ) {
        EzyArray data = EzyEntityFactory.newArrayBuilder()
            .append(command.getId())
            .append(params)
            .build();
        receive(session, data, dataSize);
    }

    @Override
    public void closeSession(EzySession session, EzyConstant reason) {
        EzyProtocolHandlerGroup handlerGroup = getHandlerGroup(session);
        if (handlerGroup != null) {
            handlerGroup.enqueueDisconnection(reason);
        }
    }

    @Override
    public void exceptionCaught(EzySession session, Throwable throwable) {
        EzyProtocolHandlerGroup handlerGroup = getHandlerGroup(session);
        if (handlerGroup != null) {
            handlerGroup.fireExceptionCaught(throwable);
        }
    }

    private EzyProtocolHandlerGroup getHandlerGroup(EzySession session) {
        if (session == null) {
            return null;
        }
        Object connection = session.getConnection();
        if (connection == null) {
            return null;
        }
        return handlerGroupManager.getHandlerGroup(connection);
    }
}
