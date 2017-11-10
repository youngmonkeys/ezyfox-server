package com.tvd12.ezyfoxserver.netty.creator.impl;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.netty.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.netty.handler.EzyChannelDataHandler;
import com.tvd12.ezyfoxserver.netty.handler.EzyDataHandler;

import io.netty.channel.ChannelHandler;
import lombok.Builder;

@Builder
public class EzyDataHandlerCreatorImpl implements EzyDataHandlerCreator {

	private EzyServerContext context;
	
	@Override
	public ChannelHandler newHandler(EzyConnectionType type) {
		EzyChannelDataHandler handler = new EzyChannelDataHandler();
		handler.setConnectionType(type);
		handler.setDataHandler(newDataHandler());
		return handler;
	}
	
	protected EzyDataHandler newDataHandler() {
		EzyDataHandler handler = new EzyDataHandler();
		handler.setContext(context);
		return handler;
	}

}
