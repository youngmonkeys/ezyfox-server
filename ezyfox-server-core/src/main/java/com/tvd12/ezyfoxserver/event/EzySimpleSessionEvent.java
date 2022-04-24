package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Getter;

@Getter
public class EzySimpleSessionEvent implements EzySessionEvent {

    protected final EzySession session;

    public EzySimpleSessionEvent(EzySession session) {
        this.session = session;
    }
}
