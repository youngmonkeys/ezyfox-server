package com.tvd12.ezyfoxserver.netty.socket;

import com.tvd12.ezyfoxserver.netty.handler.EzyAbstractHandlerGroup;

public class EzySimpleWsHandlerGroup extends EzyAbstractHandlerGroup {

	protected EzySimpleWsHandlerGroup(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractHandlerGroup.Builder {
		
		@Override
		public EzyAbstractHandlerGroup build() {
			return new EzySimpleWsHandlerGroup(this);
		}
		
	}
	
}
