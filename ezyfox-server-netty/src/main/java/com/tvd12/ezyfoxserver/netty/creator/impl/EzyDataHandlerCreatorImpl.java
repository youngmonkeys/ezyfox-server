package com.tvd12.ezyfoxserver.netty.creator.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.netty.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.netty.handler.EzyChannelDataHandler;
import com.tvd12.ezyfoxserver.netty.wrapper.EzyHandlerGroupManager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

public class EzyDataHandlerCreatorImpl implements EzyDataHandlerCreator {

	protected final EzyServerContext context;
	protected final EzyHandlerGroupManager handlerGroupManager;
	
	protected EzyDataHandlerCreatorImpl(Builder builder) {
		this.context = builder.context;
		this.handlerGroupManager = builder.handlerGroupManager;
	}
	
	@Override
	public ChannelHandler newHandler(Channel channel, EzyConnectionType type) {
		EzyChannelDataHandler handler = new EzyChannelDataHandler();
		handler.setConnectionType(type);
		handler.setHandlerGroupManager(handlerGroupManager);
		return handler;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyDataHandlerCreator> {
		
		protected EzyServerContext context;
		protected EzyHandlerGroupManager handlerGroupManager;
		
		public Builder context(EzyServerContext context) {
			this.context = context;
			return this;
		}
		
		public Builder handlerGroupManager(EzyHandlerGroupManager handlerGroupManager) {
			this.handlerGroupManager = handlerGroupManager;
			return this;
		}
		
		@Override
		public EzyDataHandlerCreator build() {
			return new EzyDataHandlerCreatorImpl(this);
		}
	}

}
