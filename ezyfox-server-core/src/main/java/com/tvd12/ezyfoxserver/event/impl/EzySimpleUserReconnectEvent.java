package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.event.EzyUserReconnectEvent;

import lombok.Getter;

@Getter
public class EzySimpleUserReconnectEvent 
		extends EzySimpleUserSessionEvent 
		implements EzyUserReconnectEvent {
    
    protected EzySimpleUserReconnectEvent(Builder builder) {
        super(builder);
    }
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
		
	    @Override
	    public EzyUserReconnectEvent build() {
	        return new EzySimpleUserReconnectEvent(this);
	    }
	}
	
}
