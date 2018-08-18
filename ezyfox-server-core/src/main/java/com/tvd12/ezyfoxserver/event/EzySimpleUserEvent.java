package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.Getter;

@Getter
public class EzySimpleUserEvent implements EzyUserEvent {

    protected EzyUser user;
    
    public EzySimpleUserEvent(EzyUser user) {
        this.user = user;
    }
    
}
