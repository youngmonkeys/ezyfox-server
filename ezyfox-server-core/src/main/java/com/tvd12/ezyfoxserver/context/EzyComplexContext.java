package com.tvd12.ezyfoxserver.context;

import java.util.Collection;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public interface EzyComplexContext 
        extends EzyContext, EzyPluginContextsFetcher, EzyAppContextsFetcher {

    EzyAppContext getAppContext(int appId);

    EzyPluginContext getPluginContext(int pluginId);
    
    void broadcast(
            EzyConstant eventType, 
            EzyEvent event, boolean catchException);
    
    void send(EzyResponse response, 
            EzySession recipient, 
            boolean encrypted, 
            EzyTransportType transportType);
    
    void send(EzyResponse response, 
            Collection<EzySession> recipients, 
            boolean encrypted,
            EzyTransportType transportType);
    
    void stream(
            byte[] bytes, 
            EzySession recipient, EzyTransportType transportType);
    
    void stream(
            byte[] bytes, 
            Collection<EzySession> recipients, EzyTransportType transportType);
    
    default void send(
            EzyResponse response, 
            EzyUser recipient, 
            boolean encrypted, EzyTransportType transportType) {
        send(response, recipient.getSessions(), encrypted, transportType);
    }
    
    // ======================= tcp/send =================
    default void send(
    		EzyResponse response, 
    		Collection<EzySession> recipients, boolean encrypted) {
        send(response, recipients, encrypted, EzyTransportType.TCP);
    }
    
    default void stream(byte[] bytes, EzySession recipient) {
        stream(bytes, recipient, EzyTransportType.TCP);
    }
    
    default void stream(byte[] bytes, Collection<EzySession> recipients) {
        stream(bytes, recipients, EzyTransportType.TCP);
    }
    
    default void send(
    		EzyResponse response, EzySession recipient, boolean encrypted) {
        send(response, recipient, encrypted, EzyTransportType.TCP);
    }
    
    default void send(
    		EzyResponse response, EzyUser recipient, boolean encrypted) {
        send(response, recipient, encrypted, EzyTransportType.TCP);
    }
    
}
