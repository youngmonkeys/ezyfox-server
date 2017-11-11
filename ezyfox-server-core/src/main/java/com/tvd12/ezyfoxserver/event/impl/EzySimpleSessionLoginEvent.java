package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.event.EzySessionLoginEvent;

import lombok.Getter;

@Getter
public class EzySimpleSessionLoginEvent 
        extends EzySimpleUserSessionEvent
		implements EzySessionLoginEvent {
    
    protected EzySimpleSessionLoginEvent(Builder builder) {
        super(builder);
    }

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
	    
	    @Override
	    public EzySessionLoginEvent build() {
	        return new EzySimpleSessionLoginEvent(this);
	    }
	    
	}
	
}
