package com.tvd12.ezyfoxserver.creator.impl;

import com.tvd12.ezyfoxserver.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import io.netty.channel.ChannelHandlerAdapter;
import lombok.Builder;

@Builder
public class EzyDataHandlerCreatorImpl implements EzyDataHandlerCreator {

	private EzySessionManager sessionManager;
	
	@Override
	public ChannelHandlerAdapter newHandler() {
		EzyDataHandler handler = new EzyDataHandler();
		handler.setSessionManager(sessionManager);
		return handler;
	}

}
