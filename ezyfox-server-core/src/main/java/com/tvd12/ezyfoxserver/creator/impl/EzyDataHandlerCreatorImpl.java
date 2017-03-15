package com.tvd12.ezyfoxserver.creator.impl;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.handler.EzyChannelUserHandler;

import io.netty.channel.ChannelHandlerAdapter;
import lombok.Builder;

@Builder
public class EzyDataHandlerCreatorImpl implements EzyDataHandlerCreator {

	private EzyServerContext context;
	
	@Override
	public ChannelHandlerAdapter newHandler() {
		EzyChannelUserHandler handler = new EzyChannelUserHandler();
		handler.setContext(context);
		return handler;
	}

}
