package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.creator.impl.EzyDataHandlerCreatorImpl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class EzyServerBootstrapCreator 
		extends EzyAbstractBootstrapCreator<EzyServerBootstrapCreator> {

	protected EzyCodecCreator codecCreator;
	
	public static EzyServerBootstrapCreator newInstance() {
		return new EzyServerBootstrapCreator();
	}
	
	public EzyServerBootstrapCreator codecCreator(EzyCodecCreator codecCreator) {
		this.codecCreator = codecCreator;
		return this;
	}

	@Override
	protected ChannelInitializer<Channel> newChannelInitializer() {
		return EzyChannelInitializer.builder()
				.codecCreator(codecCreator)
				.dataHandlerCreator(newDataHandlerCreator())
				.build();
	}
	
	protected EzyDataHandlerCreator newDataHandlerCreator() {
		return EzyDataHandlerCreatorImpl.builder()
				.context(context)
				.build();
	}
	
}
