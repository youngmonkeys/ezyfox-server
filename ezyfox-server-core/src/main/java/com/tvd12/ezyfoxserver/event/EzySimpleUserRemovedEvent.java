package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import lombok.Getter;

@Getter
public class EzySimpleUserRemovedEvent
    extends EzySimpleUserEvent
    implements EzyUserRemovedEvent {

    protected final EzyConstant reason;

    public EzySimpleUserRemovedEvent(EzyUser user, EzyConstant reason) {
        super(user);
        this.reason = reason;
    }

}
