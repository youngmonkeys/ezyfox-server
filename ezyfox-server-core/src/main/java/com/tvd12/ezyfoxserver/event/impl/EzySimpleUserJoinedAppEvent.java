package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.event.EzyUserJoinedAppEvent;

import lombok.Getter;

@Getter
public class EzySimpleUserJoinedAppEvent 
		extends EzySimpleUserSessionEvent 
		implements EzyUserJoinedAppEvent {
    
    protected EzySimpleUserJoinedAppEvent(Builder builder) {
        super(builder);
    }

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
		
	    @Override
	    public EzyUserJoinedAppEvent build() {
	        return new EzySimpleUserJoinedAppEvent(this);
	    }
	}
	
}
