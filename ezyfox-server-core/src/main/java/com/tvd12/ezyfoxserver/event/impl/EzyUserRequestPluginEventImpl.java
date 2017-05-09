package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyUserRequestPluginEventImpl 
		extends EzySimpleUserSessionEvent 
		implements EzyUserRequestPluginEvent {

	protected EzyArray data;
	
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
		protected EzyUserRequestPluginEventImpl newProduct() {
		    EzyUserRequestPluginEventImpl answer = new EzyUserRequestPluginEventImpl();
		    answer.setData(data);
		    return answer;
		}
		
	}
	
}
