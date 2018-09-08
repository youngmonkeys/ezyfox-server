package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyUser;

public class EzySimpleUserAccessAppEvent 
		extends EzySimpleUserEvent 
		implements EzyUserAccessAppEvent {

    public EzySimpleUserAccessAppEvent(EzyUser user) {
        super(user);
    }
}
