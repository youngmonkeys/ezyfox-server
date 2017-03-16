package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.event.EzyEvent;

public abstract class EzyAbstractEvent {

	protected EzyAbstractEvent(Builder<?> builder) {
	}
	
	public abstract static class Builder<B extends Builder<B>> {
		public abstract EzyEvent build();
	}
	
}
