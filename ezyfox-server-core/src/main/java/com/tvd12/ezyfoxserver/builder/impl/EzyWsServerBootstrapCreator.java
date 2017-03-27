package com.tvd12.ezyfoxserver.builder.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class EzyWsServerBootstrapCreator<C extends EzyWsServerBootstrapCreator<C>> 
		extends EzyAbstractBootstrapCreator<C> {

	@SuppressWarnings("rawtypes")
	public static EzyWsServerBootstrapCreator newInstance() {
		return new EzyWsServerBootstrapCreator();
	}

	@Override
	protected ChannelInitializer<Channel> newChannelInitializer() {
		return newWsChannelInitializerBuilder()
				.build();
	}
	
	protected EzyWsChannelInitializer.Builder newWsChannelInitializerBuilder() {
		return EzyWsChannelInitializer.builder();
	}

}
