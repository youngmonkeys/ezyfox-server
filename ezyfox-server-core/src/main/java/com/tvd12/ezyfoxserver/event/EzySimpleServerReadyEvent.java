package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.builder.EzyBuilder;

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
