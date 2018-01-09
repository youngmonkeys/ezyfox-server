package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.event.EzyUserAddedEvent;

import lombok.Getter;

@Getter
public class EzySimpleUserAddedEvent 
		extends EzySimpleUserSessionEvent 
		implements EzyUserAddedEvent {
    
    protected EzyData loginData;
    
    protected EzySimpleUserAddedEvent(Builder builder) {
        super(builder);
        this.loginData = builder.loginData;
    }

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
	    
	    protected EzyData loginData;
	    
	    public Builder loginData(EzyData data) {
	        this.loginData = data;
	        return this;
	    }
	    
	    @Override
	    public EzyUserAddedEvent build() {
	        return new EzySimpleUserAddedEvent(this);
	    }
	}
	
}
