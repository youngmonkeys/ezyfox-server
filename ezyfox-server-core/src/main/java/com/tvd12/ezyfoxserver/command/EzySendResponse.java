package com.tvd12.ezyfoxserver.command;

import java.util.Collection;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public interface EzySendResponse {

    void execute(EzyResponse response, EzySession recipient, boolean immediate);
    
    void execute(EzyResponse response, Collection<EzySession> recipients, boolean immediate);
    
    default void execute(EzyResponse response, EzySession recipient) {
        execute(response, recipient, false);
    }
    
    default void execute(EzyResponse response, Collection<EzySession> recipients) {
        execute(response, recipients, false);
    }
    
}
