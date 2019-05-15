package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyPluginContext extends EzyZoneChildContext {

    EzyPlugin getPlugin();
    
    void send(EzyData data, EzySession recipient);
    
    void send(EzyData data, Collection<EzySession> recipients);
    
    default void send(EzyData data, EzyUser recipient) {
        send(data, recipient.getSessions());
    }
    
    default void send(EzyData data, Iterable<EzyUser> recipients) {
        Set<EzySession> sessions = new HashSet<>();
        for(EzyUser user : recipients)
            sessions.addAll(user.getSessions());
        send(data, sessions);
    }
	
}
