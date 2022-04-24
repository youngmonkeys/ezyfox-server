package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import lombok.Getter;

@Getter
public class EzySimpleUserAccessedAppEvent
    implements EzyUserAccessedAppEvent, EzyUserEvent {

    protected EzyUser user;

    public EzySimpleUserAccessedAppEvent(EzyUser user) {
        this.user = user;
    }

    @Override
    public EzyUser getUser() {
        return this.user;
    }
}
