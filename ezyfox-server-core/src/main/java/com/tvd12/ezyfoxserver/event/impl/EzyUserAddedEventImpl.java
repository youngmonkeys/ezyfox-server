package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.event.EzyUserAddedEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyUserAddedEventImpl 
		extends EzySimpleUserSessionEvent 
		implements EzyUserAddedEvent {
    
    protected EzyArray loginData;

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleUserSessionEvent.Builder<Builder> {
	    
	    protected EzyArray loginData;
	    
	    public Builder loginData(EzyArray data) {
	        this.loginData = data;
	        return this;
	    }
		
		@Override
		protected EzyUserAddedEventImpl newProduct() {
		    EzyUserAddedEventImpl answer = new EzyUserAddedEventImpl();
		    answer.setLoginData(loginData);
		    return answer;
		}
	}
	
}
