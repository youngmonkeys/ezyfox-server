package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyArray;

import lombok.Getter;

@Getter
public class EzySimpleUserRequestPluginEvent 
		extends EzySimpleUserSessionEvent 
		implements EzyUserRequestPluginEvent {

	protected EzyArray data;
	
	protected EzySimpleUserRequestPluginEvent(Builder builder) {
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
		public EzyUserRequestPluginEvent build() {
		    return new EzySimpleUserRequestPluginEvent(this);
		}
		
	}
	
}
