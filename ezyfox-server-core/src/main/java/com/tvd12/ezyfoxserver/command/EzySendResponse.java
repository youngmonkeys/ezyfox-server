package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyResponse;

import java.util.Collection;

public interface EzySendResponse {

    void execute(
        EzyResponse response,
        EzySession recipient,
        boolean encrypted,
        boolean immediate,
        EzyTransportType transportType
    );

    void execute(
        EzyResponse response,
        Collection<EzySession> recipients,
        boolean encrypted,
        boolean immediate,
        EzyTransportType transportType
    );
}
