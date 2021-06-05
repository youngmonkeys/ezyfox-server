package com.tvd12.ezyfoxserver.command;

import java.util.Collection;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public interface EzySendResponse {

    void execute(
            EzyResponse response, 
            EzySession recipient, 
            boolean encrypted,
            boolean immediate, EzyTransportType transportType);
    
    void execute(EzyResponse response, 
            Collection<EzySession> recipients, 
            boolean encrypted,
            boolean immediate, EzyTransportType transportType);
    
    default void execute(EzyResponse response, 
            EzySession recipient,
            boolean encrypted,
            EzyTransportType transportType) {
        execute(response, recipient, false, encrypted, transportType);
    }
    
    default void execute(EzyResponse response, 
            Collection<EzySession> recipients, 
            boolean encrypted,
            EzyTransportType transportType) {
        execute(response, recipients, false, encrypted, transportType);
    }

    default void execute(
    		EzyResponse response, 
    		EzySession recipient, boolean encrypted) {
        execute(response, recipient, encrypted, EzyTransportType.TCP);
    }
    
    default void execute(
    		EzyResponse response, 
    		Collection<EzySession> recipients, boolean encrypted) {
        execute(response, recipients, encrypted, EzyTransportType.TCP);
    }
    
    default void execute(
    		EzyResponse response, 
    		EzySession recipient, boolean immediate, boolean encrypted) {
        execute(response, recipient, immediate, encrypted, EzyTransportType.TCP);
    }
    
    default void execute(
    		EzyResponse response, 
    		Collection<EzySession> recipients, 
    		boolean immediate, boolean encrypted) {
        execute(response, recipients, immediate, encrypted, EzyTransportType.TCP);
    }
    
    
}
