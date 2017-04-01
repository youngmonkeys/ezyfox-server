package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.builder.impl.EzyChannelInitializer.Builder;

public class EzyWsServerBootstrapCreator<C extends EzyWsServerBootstrapCreator<C>> 
		extends EzyServerBootstrapCreator<C> {

	public static EzyWsServerBootstrapCreator<?> newInstance() {
		return new EzyWsServerBootstrapCreator<>();
	}

	@Override
	protected Builder newChannelInitializerBuilder() {
		return newWsChannelInitializerBuilder();
	}
	
	protected EzyWsChannelInitializer.Builder newWsChannelInitializerBuilder() {
		return EzyWsChannelInitializer.builder();
	}

}
