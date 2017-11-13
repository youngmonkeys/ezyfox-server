package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.event.EzyUserDisconnectEvent;

import lombok.Getter;

@Getter
public class EzySimpleUserDisconnectEvent 
		extends EzySimpleUserEvent 
		implements EzyUserDisconnectEvent {

	protected EzyConstant reason;
	
	protected EzySimpleUserDisconnectEvent(Builder builder) {
	    super(builder);
	    this.reason = builder.reason;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserEvent.Builder<Builder> {
	    protected EzyConstant reason;
		
		public Builder reason(EzyConstant reason) {
			this.reason = reason;
			return this;
		}
		
		@Override
		public EzyUserDisconnectEvent build() {
		    return new EzySimpleUserDisconnectEvent(this);
		}
	}
	
}
