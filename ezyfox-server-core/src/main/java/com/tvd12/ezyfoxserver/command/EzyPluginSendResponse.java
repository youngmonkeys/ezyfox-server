package com.tvd12.ezyfoxserver.command;

import java.util.Collection;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyPluginSendResponse extends EzyChildSendResponse {

    void execute(EzyData data, EzySession recipient, boolean withName);
    
    void execute(EzyData data, Collection<EzySession> recipients, boolean withName);
    
    @Override
    default void execute(EzyData data, EzySession recipient) {
        execute(data, recipient, true);
    }
    
    @Override
    default void execute(EzyData data, Collection<EzySession> recipients) {
        execute(data, recipients, true);
    }
    
}
