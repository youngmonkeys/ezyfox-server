package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.event.EzySessionRemovedEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySessionRemovedEventImpl 
		extends EzySimpleUserSessionEvent 
		implements EzySessionRemovedEvent {

    protected EzyConstant reason;
    
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
		
	    protected EzyConstant reason;
	    
	    public Builder reason(EzyConstant reason) {
	        this.reason = reason;
	        return this;
	    }
	    
		@Override
		protected EzySessionRemovedEventImpl newProduct() {
		    EzySessionRemovedEventImpl answer = new EzySessionRemovedEventImpl();
		    answer.setReason(reason);
		    return answer;
		}
		
	}
	
}
