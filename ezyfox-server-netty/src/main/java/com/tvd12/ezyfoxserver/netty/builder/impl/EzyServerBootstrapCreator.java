package com.tvd12.ezyfoxserver.netty.builder.impl;

import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.netty.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.netty.creator.impl.EzyDataHandlerCreatorImpl;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

@SuppressWarnings({"unchecked"})
public class EzyServerBootstrapCreator<C extends EzyServerBootstrapCreator<C>> 
		extends EzyAbstractBootstrapCreator<C> {

	protected int maxRequestSize;
	protected EzyCodecCreator codecCreator;
	protected EzySessionTicketsQueue sessionTicketsQueue;
	
	public static EzyServerBootstrapCreator<?> newInstance() {
		return new EzyServerBootstrapCreator<>();
	}
	
	public C maxRequestSize(int maxRequestSize) {
		this.maxRequestSize = maxRequestSize;
		return (C)this;
	}
	
	public C codecCreator(EzyCodecCreator codecCreator) {
		this.codecCreator = codecCreator;
		return (C)this;
	}
	
	public C sessionTicketsQueue(EzySessionTicketsQueue sessionTicketsQueue) {
		this.sessionTicketsQueue = sessionTicketsQueue;
		return (C)this;
	}

	@Override
	protected ChannelInitializer<Channel> newChannelInitializer() {
		return newChannelInitializerBuilder()
				.codecCreator(codecCreator)
				.maxRequestSize(maxRequestSize)
				.dataHandlerCreator(newDataHandlerCreator())
				.build();
	}
	
	protected EzyChannelInitializer.Builder newChannelInitializerBuilder() {
		return EzyChannelInitializer.builder();
	}
	
	protected EzyDataHandlerCreator newDataHandlerCreator() {
		return EzyDataHandlerCreatorImpl.builder()
				.context(context)
				.sessionTicketsQueue(sessionTicketsQueue)
				.build();
	}
	
}
