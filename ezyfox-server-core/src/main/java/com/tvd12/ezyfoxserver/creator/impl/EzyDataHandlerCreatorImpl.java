package com.tvd12.ezyfoxserver.creator.impl;

import com.tvd12.ezyfoxserver.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.handler.EzyChannelSessionHandler;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import io.netty.channel.ChannelHandlerAdapter;
import lombok.Builder;

@Builder
public class EzyDataHandlerCreatorImpl implements EzyDataHandlerCreator {

	private EzyControllers controllers;
	private EzySessionManager sessionManager;
	
	@Override
	public ChannelHandlerAdapter newHandler() {
		EzyChannelSessionHandler handler = new EzyChannelSessionHandler();
		handler.setControllers(controllers);
		handler.setSessionManager(sessionManager);
		return handler;
	}

}
