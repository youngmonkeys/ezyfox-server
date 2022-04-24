package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import lombok.Getter;

@Getter
public class EzySimpleUserAccessAppEvent
    extends EzySimpleUserEvent
    implements EzyUserAccessAppEvent {

    protected final EzyArray output;

    public EzySimpleUserAccessAppEvent(EzyUser user) {
        super(user);
        this.output = EzyEntityFactory.newArray();
    }
}
