package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.event.EzyUserAccessAppEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyUserAccessAppEventImpl 
		extends EzySimpleUserEvent 
		implements EzyUserAccessAppEvent {

	protected EzyData output;
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserEvent.Builder<Builder> {
	    
	    @Override
	    public EzyUserAccessAppEvent build() {
	        return (EzyUserAccessAppEvent)super.build();
	    }
	    
		@Override
		protected EzyUserAccessAppEventImpl newProduct() {
		    return new EzyUserAccessAppEventImpl();
		}
	}
	
}
