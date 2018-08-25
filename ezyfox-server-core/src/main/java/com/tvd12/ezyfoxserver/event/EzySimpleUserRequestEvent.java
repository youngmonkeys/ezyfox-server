package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.Getter;

@Getter
public class EzySimpleUserRequestEvent 
		extends EzySimpleUserSessionEvent 
		implements EzyUserRequestAppEvent {

    protected EzyArray data;
    
	public EzySimpleUserRequestEvent(EzyUser user, EzySession session, EzyArray data) {
        super(user, session);
        this.data = data;
    }

}
