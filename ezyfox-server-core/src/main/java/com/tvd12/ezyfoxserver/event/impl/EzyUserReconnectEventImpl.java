package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.event.EzyUserReconnectEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyUserReconnectEventImpl 
		extends EzySimpleUserSessionEvent 
		implements EzyUserReconnectEvent {
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
		
		@Override
		protected EzyUserReconnectEventImpl newProduct() {
		    return new EzyUserReconnectEventImpl();
		}
	}
	
}
