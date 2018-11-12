package com.tvd12.ezyfoxserver.context;

import java.util.Collection;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public interface EzyComplexContext 
        extends EzyContext, EzyPluginContextsFetcher, EzyAppContextsFetcher {

    EzyAppContext getAppContext(int appId);

    EzyPluginContext getPluginContext(int pluginId);
    
    void broadcast(EzyConstant eventType, EzyEvent event);
    
    void send(EzyResponse response, 
            EzySession recipient, boolean immediate);
    
    void send(EzyResponse response, 
            Collection<EzySession> recipients, boolean immediate);
    
    default void send(EzyResponse response, EzySession recipient) {
        send(response, recipient, false);
    }
    
    default void send(EzyResponse response, Collection<EzySession> recipients) {
        send(response, recipients, false);
    }

}
