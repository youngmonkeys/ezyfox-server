package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;

import lombok.Getter;

@Getter
public class EzySimpleUserRequestAppEvent 
		extends EzySimpleUserSessionEvent 
		implements EzyUserRequestAppEvent {

	protected EzyArray data;
	
	public EzySimpleUserRequestAppEvent(Builder builder) {
	    super(builder);
	    this.data = builder.data;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
	    
	    protected EzyArray data;
		
		public Builder data(EzyArray data) {
			this.data = data;
			return this;
		}
		
		@Override
		public EzyUserRequestAppEvent build() {
		    return new EzySimpleUserRequestAppEvent(this);
		}
		
	}
	
}
