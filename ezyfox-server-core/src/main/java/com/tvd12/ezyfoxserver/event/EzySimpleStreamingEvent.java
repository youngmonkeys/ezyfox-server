package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import lombok.Getter;

public class EzySimpleStreamingEvent extends EzySimpleUserSessionEvent implements EzyStreamingEvent {

    @Getter
    private final byte[] bytes;

    public EzySimpleStreamingEvent(EzyUser user, EzySession session, byte[] bytes) {
        super(user, session);
        this.bytes = bytes;
    }

}
