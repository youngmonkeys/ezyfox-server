package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.creator.impl.EzyDataHandlerCreatorImpl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class EzyServerBootstrapCreator<C extends EzyServerBootstrapCreator<C>> 
		extends EzyAbstractBootstrapCreator<C> {

	protected EzyCodecCreator codecCreator;
	
	public static EzyServerBootstrapCreator<?> newInstance() {
		return new EzyServerBootstrapCreator<>();
	}
	
	public C codecCreator(EzyCodecCreator codecCreator) {
		this.codecCreator = codecCreator;
		return getThis();
	}

	@Override
	protected ChannelInitializer<Channel> newChannelInitializer() {
		return newChannelInitializerBuilder()
				.codecCreator(codecCreator)
				.dataHandlerCreator(newDataHandlerCreator())
				.build();
	}
	
	protected EzyChannelInitializer.Builder newChannelInitializerBuilder() {
		return EzyChannelInitializer.builder();
	}
	
	protected EzyDataHandlerCreator newDataHandlerCreator() {
		return EzyDataHandlerCreatorImpl.builder()
				.context(context)
				.build();
	}
	
}
