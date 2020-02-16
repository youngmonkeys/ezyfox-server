package com.tvd12.ezyfoxserver.command;

import java.util.Collection;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyChildSendResponse {

    void execute(
            EzyData data, 
            EzySession recipient, EzyTransportType transportType);
    
    void execute(
            EzyData data, 
            Collection<EzySession> recipients, EzyTransportType transportType);
    
    default void execute(EzyData data, EzySession recipient) {
        execute(data, recipient, EzyTransportType.TCP);
    }
    
    default void execute(EzyData data, Collection<EzySession> recipients) {
        execute(data, recipients, EzyTransportType.TCP);
    }
    
}
