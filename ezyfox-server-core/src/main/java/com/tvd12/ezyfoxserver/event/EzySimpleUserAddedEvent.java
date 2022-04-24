package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import lombok.Getter;

@Getter
public class EzySimpleUserAddedEvent
    extends EzySimpleUserSessionEvent
    implements EzyUserAddedEvent {

    protected final EzyData loginData;

    public EzySimpleUserAddedEvent(EzyUser user, EzySession session, EzyData data) {
        super(user, session);
        this.loginData = data;
    }

}
