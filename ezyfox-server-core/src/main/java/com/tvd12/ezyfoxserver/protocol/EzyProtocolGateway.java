package com.tvd12.ezyfoxserver.protocol;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyProtocolGateway {

    EzySession openSession(EzyProtocolChannel channel);

    EzySession getSession(Object connection);

    void receive(EzySession session, EzyArray data, int dataSize);

    void receive(
        EzySession session,
        EzyCommand command,
        EzyArray params,
        int dataSize
    );

    void closeSession(EzySession session, EzyConstant reason);

    default void closeSession(EzySession session) {
        closeSession(session, EzyDisconnectReason.UNKNOWN);
    }

    void exceptionCaught(EzySession session, Throwable throwable);
}
