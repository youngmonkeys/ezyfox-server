package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutorServiceFetcher;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;

import java.util.Collection;

public interface EzyZoneChildContext extends
    EzyContext,
    EzyExecutorServiceFetcher {

    EzyZoneContext getParent();

    void send(
        EzyData data,
        EzySession recipient,
        boolean encrypted,
        EzyTransportType transportType
    );

    void send(
        EzyData data,
        Collection<EzySession> recipients,
        boolean encrypted,
        EzyTransportType transportType
    );

    // ======================= tcp/send =================
    default void send(EzyData data, EzySession recipient, boolean encrypted) {
        send(data, recipient, encrypted, EzyTransportType.TCP);
    }
}
