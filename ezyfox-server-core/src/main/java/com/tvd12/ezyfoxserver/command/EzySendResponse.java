package com.tvd12.ezyfoxserver.command;

import java.util.Collection;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public interface EzySendResponse {

    void execute(
            EzyResponse response, 
            EzySession recipient, 
            boolean immediate, EzyTransportType transportType);
    
    void execute(EzyResponse response, 
            Collection<EzySession> recipients, 
            boolean immediate, EzyTransportType transportType);
    
    default void execute(EzyResponse response, 
            EzySession recipient, EzyTransportType transportType) {
        execute(response, recipient, false, transportType);
    }
    
    default void execute(EzyResponse response, 
            Collection<EzySession> recipients, EzyTransportType transportType) {
        execute(response, recipients, false, transportType);
    }

    default void execute(EzyResponse response, EzySession recipient) {
        execute(response, recipient, EzyTransportType.TCP);
    }
    
    default void execute(EzyResponse response, Collection<EzySession> recipients) {
        execute(response, recipients, EzyTransportType.TCP);
    }
    
    default void execute(EzyResponse response, EzySession recipient, boolean immediate) {
        execute(response, recipient, immediate, EzyTransportType.TCP);
    }
    
    default void execute(EzyResponse response, Collection<EzySession> recipients, boolean immediate) {
        execute(response, recipients, immediate, EzyTransportType.TCP);
    }
    
    
}
