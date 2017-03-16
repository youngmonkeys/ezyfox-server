package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzyRequestAppEvent;

import lombok.Getter;

public class EzyRequestAppEventImpl 
		extends EzyAbstractEvent 
		implements EzyRequestAppEvent {

	@Getter
	private EzyUser user;
	@Getter
	private EzyData data;
	
	protected EzyRequestAppEventImpl(Builder builder) {
		super(builder);
		this.user = builder.user;
		this.data = builder.data;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractEvent.Builder<Builder> {
		private EzyUser user;
		private EzyData data;
		
		public Builder user(EzyUser user) {
			this.user = user;
			return this;
		}
		
		public Builder data(EzyData data) {
			this.data = data;
			return this;
		}
		
		@Override
		public EzyEvent build() {
			return new EzyRequestAppEventImpl(this);
		}
	}
	
}
