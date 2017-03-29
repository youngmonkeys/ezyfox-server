package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyDisconnectEvent;

import lombok.Getter;

public class EzyDisconnectEventImpl 
		extends EzyAbstractEvent 
		implements EzyDisconnectEvent {

	@Getter
	private EzyUser user;
	@Getter
	private EzyConstant reason;
	
	protected EzyDisconnectEventImpl(Builder builder) {
		super(builder);
		this.user = builder.user;
		this.reason = builder.reason;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractEvent.Builder<Builder> {
		private EzyUser user;
		private EzyConstant reason;
		
		public Builder user(EzyUser user) {
			this.user = user;
			return this;
		}
		
		public Builder reason(EzyConstant reason) {
			this.reason = reason;
			return this;
		}
		
		@Override
		public EzyDisconnectEvent build() {
			return new EzyDisconnectEventImpl(this);
		}
	}
	
}
