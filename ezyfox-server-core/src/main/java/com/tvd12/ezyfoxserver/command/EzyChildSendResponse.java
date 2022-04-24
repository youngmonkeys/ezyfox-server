package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;

import java.util.Collection;

public interface EzyChildSendResponse {

    void execute(
        EzyData data,
        EzySession recipient,
        boolean encrypted, EzyTransportType transportType);

    void execute(
        EzyData data,
        Collection<EzySession> recipients,
        boolean encrypted, EzyTransportType transportType);

    default void execute(EzyData data, EzySession recipient, boolean encrypted) {
        execute(data, recipient, encrypted, EzyTransportType.TCP);
    }

    default void execute(EzyData data, Collection<EzySession> recipients, boolean encrypted) {
        execute(data, recipients, encrypted, EzyTransportType.TCP);
    }

}
