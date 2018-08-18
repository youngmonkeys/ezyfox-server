package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.Getter;

@Getter
public class EzySimpleSessionLoginEvent 
        extends EzySimpleUserSessionEvent
		implements EzySessionLoginEvent {

    public EzySimpleSessionLoginEvent(EzyUser user, EzySession session) {
        super(user, session);
    }
    
}
