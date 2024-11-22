package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import lombok.Getter;

@Getter
public class EzySimpleUserExitedAppEvent
    extends EzySimpleUserEvent
    implements EzyUserExitedAppEvent {

    public EzySimpleUserExitedAppEvent(EzyUser user) {
        super(user);
    }
}
