package com.tvd12.ezyfoxserver.netty.handler;

public class EzySimpleNettyHandlerGroup extends EzyAbstractHandlerGroup {

	protected EzySimpleNettyHandlerGroup(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractHandlerGroup.Builder {
		
		@Override
		public EzyAbstractHandlerGroup build() {
			return new EzySimpleNettyHandlerGroup(this);
		}
		
	}
	
}
