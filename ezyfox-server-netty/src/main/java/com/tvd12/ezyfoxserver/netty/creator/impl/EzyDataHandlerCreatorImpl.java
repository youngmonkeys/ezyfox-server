package com.tvd12.ezyfoxserver.netty.creator.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.netty.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.netty.handler.EzyChannelDataHandler;
import com.tvd12.ezyfoxserver.netty.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;

import io.netty.channel.ChannelHandler;

public class EzyDataHandlerCreatorImpl implements EzyDataHandlerCreator {

	protected EzyServerContext context;
	protected EzySessionTicketsQueue sessionTicketsQueue;
	
	protected EzyDataHandlerCreatorImpl(Builder builder) {
		this.context = builder.context;
		this.sessionTicketsQueue = builder.sessionTicketsQueue;
	}
	
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
		handler.setSessionTicketsQueue(sessionTicketsQueue);
		return handler;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyDataHandlerCreator> {
		
		protected EzyServerContext context;
		protected EzySessionTicketsQueue sessionTicketsQueue;
		
		public Builder context(EzyServerContext context) {
			this.context = context;
			return this;
		}
		
		public Builder sessionTicketsQueue(EzySessionTicketsQueue sessionTicketsQueue) {
			this.sessionTicketsQueue = sessionTicketsQueue;
			return this;
		}
		
		@Override
		public EzyDataHandlerCreator build() {
			return new EzyDataHandlerCreatorImpl(this);
		}
	}

}
