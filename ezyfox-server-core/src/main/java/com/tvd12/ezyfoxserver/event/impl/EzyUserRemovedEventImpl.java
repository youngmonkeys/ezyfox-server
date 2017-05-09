package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.event.EzyUserRemovedEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyUserRemovedEventImpl 
		extends EzySimpleUserEvent 
		implements EzyUserRemovedEvent {

	protected EzyConstant reason;
	
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
		protected EzyUserRemovedEventImpl newProduct() {
		    EzyUserRemovedEventImpl answer = new EzyUserRemovedEventImpl();
		    answer.setReason(reason);
		    return answer;
		}
	}
	
}
