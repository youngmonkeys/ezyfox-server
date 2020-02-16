package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
            boolean immediate, EzyTransportType transportType);
    
    void send(EzyResponse response, 
            Collection<EzySession> recipients, 
            boolean immediate, EzyTransportType transportType);
    
    void stream(
            byte[] bytes, 
            EzySession recipient, EzyTransportType transportType);
    
    void stream(
            byte[] bytes, 
            Collection<EzySession> recipients, EzyTransportType transportType);
    
    default void send(
            EzyResponse response, 
            EzySession recipient, EzyTransportType transportType) {
        send(response, recipient, false, transportType);
    }
    
    default void send(
            EzyResponse response, 
            Collection<EzySession> recipients, EzyTransportType transportType) {
        send(response, recipients, false, transportType);
    }
    
    default void send(
            EzyResponse response, 
            EzyUser recipient, EzyTransportType transportType) {
        send(response, recipient.getSessions(), transportType);
    }
    
    default void send(
            EzyResponse response, 
            Iterable<EzyUser> recipients, EzyTransportType transportType) {
        Set<EzySession> sessions = new HashSet<>();
        for(EzyUser user : recipients)
            sessions.addAll(user.getSessions());
        send(response, sessions, transportType);
    }
    
    // ======================= tcp/send =================
    default void send(EzyResponse response, Collection<EzySession> recipients) {
        send(response, recipients, EzyTransportType.TCP);
    }
    
    default void stream(byte[] bytes, EzySession recipient) {
        stream(bytes, recipient, EzyTransportType.TCP);
    }
    
    default void stream(byte[] bytes, Collection<EzySession> recipients) {
        stream(bytes, recipients, EzyTransportType.TCP);
    }
    
    default void send(EzyResponse response, EzySession recipient) {
        send(response, recipient, EzyTransportType.TCP);
    }
    
    default void send(EzyResponse response, EzyUser recipient) {
        send(response, recipient, EzyTransportType.TCP);
    }
    
    default void send(EzyResponse response, Iterable<EzyUser> recipients) {
        send(response, recipients, EzyTransportType.TCP);
    }
    
    default void send(EzyResponse response, EzySession recipient, boolean immediate) {
        send(response, recipient, immediate, EzyTransportType.TCP);
    }
    
    default void send(EzyResponse response, Collection<EzySession> recipients, boolean immediate) {
        send(response, recipients, immediate, EzyTransportType.TCP);
    }
    
}
