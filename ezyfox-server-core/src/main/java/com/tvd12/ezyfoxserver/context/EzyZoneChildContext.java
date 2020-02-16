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
            EzySession recipient, EzyTransportType transportType);
    
    void send(
            EzyData data, 
            Collection<EzySession> recipients, EzyTransportType transportType);
    
    default void send(
            EzyData data, 
            EzyUser recipient, EzyTransportType transportType) {
        send(data, recipient.getSessions(), transportType);
    }
    
    default void send(
            EzyData data, 
            Iterable<EzyUser> recipients, EzyTransportType transportType) {
        Set<EzySession> sessions = new HashSet<>();
        for(EzyUser user : recipients)
            sessions.addAll(user.getSessions());
        send(data, sessions, transportType);
    }
    
    // ======================= tcp/send =================
    default void send(EzyData data, EzySession recipient) {
        send(data, recipient, EzyTransportType.TCP);
    }
    
    default void send(EzyData data, Collection<EzySession> recipients) {
        send(data, recipients, EzyTransportType.TCP);
    }
    
    default void send(EzyData data, EzyUser recipient) {
        send(data, recipient, EzyTransportType.TCP);
    }
    
    default void send(EzyData data, Iterable<EzyUser> recipients) {
        send(data, recipients, EzyTransportType.TCP);
    }
    
}
