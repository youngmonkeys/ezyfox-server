package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import lombok.Getter;

@Getter
public class EzySimpleSessionRemovedEvent
    extends EzySimpleUserSessionEvent
    implements EzySessionRemovedEvent {

    protected final EzyConstant reason;

    public EzySimpleSessionRemovedEvent(
        EzyUser user, EzySession session, EzyConstant reason) {
        super(user, session);
        this.reason = reason;
    }

}
