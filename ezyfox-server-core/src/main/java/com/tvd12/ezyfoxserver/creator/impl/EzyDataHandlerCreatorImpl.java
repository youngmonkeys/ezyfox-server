package com.tvd12.ezyfoxserver.creator.impl;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.handler.EzyChannelDataHandler;
import com.tvd12.ezyfoxserver.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.handler.EzyUserHandler;

import io.netty.channel.ChannelHandlerAdapter;
import lombok.Builder;

@Builder
public class EzyDataHandlerCreatorImpl implements EzyDataHandlerCreator {

	private EzyServerContext context;
	
	@Override
	public ChannelHandlerAdapter newHandler() {
		EzyChannelDataHandler handler = new EzyChannelDataHandler();
		handler.setDataHandler(newDataHandler());
		return handler;
	}
	
	protected EzyDataHandler newDataHandler() {
		EzyUserHandler handler = new EzyUserHandler();
		handler.setContext(context);
		return handler;
	}

}
