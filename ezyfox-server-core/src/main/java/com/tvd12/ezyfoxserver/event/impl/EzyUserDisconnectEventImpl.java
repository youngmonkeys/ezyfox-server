package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.event.EzyUserDisconnectEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyUserDisconnectEventImpl 
		extends EzySimpleUserEvent 
		implements EzyUserDisconnectEvent {

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
		public EzyUserDisconnectEventImpl newProduct() {
		    EzyUserDisconnectEventImpl answer = new EzyUserDisconnectEventImpl();
		    answer.setReason(reason);
		    return answer;
		}
	}
	
}
