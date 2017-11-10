package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyUserRequestAppEventImpl 
		extends EzySimpleUserSessionEvent 
		implements EzyUserRequestAppEvent {

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
		protected EzyUserRequestAppEventImpl newProduct() {
		    EzyUserRequestAppEventImpl answer = new EzyUserRequestAppEventImpl();
		    answer.setData(data);
		    return answer;
		}
		
	}
	
}
