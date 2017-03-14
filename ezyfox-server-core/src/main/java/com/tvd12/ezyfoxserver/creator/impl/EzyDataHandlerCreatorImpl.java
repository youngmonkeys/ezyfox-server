package com.tvd12.ezyfoxserver.creator.impl;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.handler.EzyChannelSessionHandler;

import io.netty.channel.ChannelHandlerAdapter;
import lombok.Builder;

@Builder
public class EzyDataHandlerCreatorImpl implements EzyDataHandlerCreator {

	private EzyServerContext context;
	
	@Override
	public ChannelHandlerAdapter newHandler() {
		EzyChannelSessionHandler handler = new EzyChannelSessionHandler();
		handler.setContext(context);
		return handler;
	}

}
