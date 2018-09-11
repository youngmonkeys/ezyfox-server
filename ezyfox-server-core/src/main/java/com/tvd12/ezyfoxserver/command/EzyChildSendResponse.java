package com.tvd12.ezyfoxserver.command;

import java.util.Collection;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyChildSendResponse {

    void execute(EzyData data, EzySession recipient);
    
    void execute(EzyData data, Collection<EzySession> recipients);
    
}
