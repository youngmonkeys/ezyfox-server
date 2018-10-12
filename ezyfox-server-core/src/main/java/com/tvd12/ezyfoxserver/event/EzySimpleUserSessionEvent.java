package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.Getter;

@Getter
public class EzySimpleUserSessionEvent implements EzyUserSessionEvent {

    protected final EzyUser user;
    protected final EzySession session;
    
    public EzySimpleUserSessionEvent(EzyUser user, EzySession session) {
        this.user = user;
        this.session = session;
    }
    
}
