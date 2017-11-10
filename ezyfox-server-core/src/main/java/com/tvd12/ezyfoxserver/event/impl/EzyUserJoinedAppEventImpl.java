package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.event.EzyUserJoinedAppEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyUserJoinedAppEventImpl 
		extends EzySimpleUserSessionEvent 
		implements EzyUserJoinedAppEvent {

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
		
		@Override
		protected EzyUserJoinedAppEventImpl newProduct() {
		    return new EzyUserJoinedAppEventImpl();
		}
	}
	
}
