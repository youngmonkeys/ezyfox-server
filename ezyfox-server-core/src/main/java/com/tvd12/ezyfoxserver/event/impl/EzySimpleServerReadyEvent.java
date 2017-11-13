package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;

import lombok.Getter;

@Getter
public class EzySimpleServerReadyEvent implements EzyServerReadyEvent {
	
    protected EzySimpleServerReadyEvent(Builder builder) {
    }
    
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyServerReadyEvent> {
		
		@Override
		public EzyServerReadyEvent build() {
		    return new EzySimpleServerReadyEvent(this);
		}
	}
	
}
