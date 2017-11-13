package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.event.EzyUserRemovedEvent;

import lombok.Getter;

@Getter
public class EzySimpleUserRemovedEvent 
		extends EzySimpleUserEvent 
		implements EzyUserRemovedEvent {

	protected EzyConstant reason;
	
	protected EzySimpleUserRemovedEvent(Builder builder) {
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
		public EzyUserRemovedEvent build() {
		    return new EzySimpleUserRemovedEvent(this);
		}
	}
	
}
