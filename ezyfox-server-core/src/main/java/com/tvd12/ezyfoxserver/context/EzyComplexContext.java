package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public interface EzyComplexContext 
        extends EzyContext, EzyPluginContextsFetcher, EzyAppContextsFetcher {

    EzyAppContext getAppContext(int appId);

    EzyPluginContext getPluginContext(int pluginId);
    
    void broadcast(EzyConstant eventType, EzyEvent event, boolean catchException);
    
    void send(EzyResponse response, 
            EzySession recipient, boolean immediate);
    
    void send(EzyResponse response, 
            Collection<EzySession> recipients, boolean immediate);
    
    void stream(byte[] bytes, EzySession recipient);
    
    void stream(byte[] bytes, Collection<EzySession> recipients);
    
    default void send(EzyResponse response, EzySession recipient) {
        send(response, recipient, false);
    }
    
    default void send(EzyResponse response, Collection<EzySession> recipients) {
        send(response, recipients, false);
    }
    
    default void send(EzyResponse response, EzyUser recipient) {
        send(response, recipient.getSessions());
    }
    
    default void send(EzyResponse response, Iterable<EzyUser> recipients) {
        Set<EzySession> sessions = new HashSet<>();
        for(EzyUser user : recipients)
            sessions.addAll(user.getSessions());
        send(response, sessions);
    }
    
}
