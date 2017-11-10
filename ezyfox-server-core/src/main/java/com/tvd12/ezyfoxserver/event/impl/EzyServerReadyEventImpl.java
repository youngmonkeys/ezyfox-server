package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyServerReadyEventImpl implements EzyServerReadyEvent {
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyServerReadyEvent> {
		
		@Override
		public EzyServerReadyEvent build() {
		    EzyServerReadyEventImpl answer = new EzyServerReadyEventImpl();
		    return answer;
		}
	}
	
}
