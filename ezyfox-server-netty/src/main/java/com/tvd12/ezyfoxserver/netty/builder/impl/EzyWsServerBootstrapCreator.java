package com.tvd12.ezyfoxserver.netty.builder.impl;

import com.tvd12.ezyfoxserver.netty.builder.impl.EzyChannelInitializer.Builder;

@SuppressWarnings({"unchecked"})
public class EzyWsServerBootstrapCreator<C extends EzyWsServerBootstrapCreator<C>> 
		extends EzyServerBootstrapCreator<C> {
	
	protected int maxFrameSize;

	public static EzyWsServerBootstrapCreator<?> newInstance() {
		return new EzyWsServerBootstrapCreator<>();
	}
	
	public C maxFrameSize(int maxFrameSize) {
		this.maxFrameSize = maxFrameSize;
		return (C)this;
	}

	@Override
	protected Builder newChannelInitializerBuilder() {
		return newWsChannelInitializerBuilder().maxFrameSize(maxFrameSize);
	}
	
	protected EzyWsChannelInitializer.Builder newWsChannelInitializerBuilder() {
		return EzyWsChannelInitializer.builder();
	}

}
