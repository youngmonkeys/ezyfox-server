package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyZoneChildContext extends EzyContext {

    EzyZoneContext getParent();
    
    void send(
            EzyData data, 
            EzySession recipient,
            boolean encrypted, EzyTransportType transportType);
    
    void send(
            EzyData data, 
            Collection<EzySession> recipients, 
            boolean encrypted, EzyTransportType transportType);
    
    default void send(
            EzyData data, 
            EzyUser recipient, 
            boolean encrypted, EzyTransportType transportType) {
        send(data, recipient.getSessions(), encrypted, transportType);
    }
    
    default void send(
            EzyData data, 
            Iterable<EzyUser> recipients, 
            boolean encrypted, EzyTransportType transportType) {
        Set<EzySession> sessions = new HashSet<>();
        for(EzyUser user : recipients)
            sessions.addAll(user.getSessions());
        send(data, sessions, encrypted, transportType);
    }
    
    // ======================= tcp/send =================
    default void send(EzyData data, EzySession recipient, boolean encrypted) {
        send(data, recipient, encrypted, EzyTransportType.TCP);
    }
    
    default void send(EzyData data, Collection<EzySession> recipients, boolean encrypted) {
        send(data, recipients, encrypted, EzyTransportType.TCP);
    }
    
    default void send(EzyData data, EzyUser recipient, boolean encrypted) {
        send(data, recipient, encrypted, EzyTransportType.TCP);
    }
    
    default void send(EzyData data, Iterable<EzyUser> recipients, boolean encrypted) {
        send(data, recipients, encrypted, EzyTransportType.TCP);
    }
    
}
