package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.event.EzySessionLoginEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySessionLoginEventImpl 
        extends EzySimpleUserSessionEvent
		implements EzySessionLoginEvent {

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
	    
	    @Override
	    protected EzySessionLoginEventImpl newProduct() {
	        return new EzySessionLoginEventImpl();
	    }
	    
	}
	
}
