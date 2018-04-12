package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyData;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzySimpleUserAccessAppEvent 
		extends EzySimpleUserEvent 
		implements EzyUserAccessAppEvent {

    @Setter
	protected EzyData output;
	
	protected EzySimpleUserAccessAppEvent(Builder builder) {
	    super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserEvent.Builder<Builder> {
	    
	    @Override
	    public EzyUserAccessAppEvent build() {
	        return new EzySimpleUserAccessAppEvent(this);
	    }
	}
	
}
