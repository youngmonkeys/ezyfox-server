package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;

import lombok.Getter;

public class EzyServerReadyEventImpl 
		extends EzyAbstractEvent 
		implements EzyServerReadyEvent {

	@Getter
	private EzyServer server;
	
	protected EzyServerReadyEventImpl(Builder builder) {
		super(builder);
		this.server = builder.server;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractEvent.Builder<Builder> {
		private EzyServer server;
		
		public Builder server(EzyServer server) {
			this.server = server;
			return this;
		}
		
		@Override
		public EzyEvent build() {
			return new EzyServerReadyEventImpl(this);
		}
	}
	
}
