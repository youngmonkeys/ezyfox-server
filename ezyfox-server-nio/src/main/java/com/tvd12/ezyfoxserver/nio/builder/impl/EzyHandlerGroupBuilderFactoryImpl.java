package com.tvd12.ezyfoxserver.nio.builder.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzySimpleNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.nio.websocket.EzySimpleWsHandlerGroup;

public class EzyHandlerGroupBuilderFactoryImpl implements EzyHandlerGroupBuilderFactory {

	private EzySessionTicketsQueue socketSessionTicketsQueue;
	private EzySessionTicketsQueue websocketSessionTicketsQueue;
	
	public EzyHandlerGroupBuilderFactoryImpl(Builder builder) {
		this.socketSessionTicketsQueue = builder.socketSessionTicketsQueue;
		this.websocketSessionTicketsQueue = builder.websocketSessionTicketsQueue;
	}
	
	@Override
	public EzyAbstractHandlerGroup.Builder newBuilder(EzyConnectionType type) {
		switch (type) {
		case SOCKET:
			return newBuilderBySocketType();
		default:
			return newBuilderByWebSocketType();
		}
	}
	
	private EzyAbstractHandlerGroup.Builder newBuilderBySocketType() {
		EzyAbstractHandlerGroup.Builder builder = EzySimpleNioHandlerGroup.builder();
		builder.sessionTicketsQueue(socketSessionTicketsQueue);
		return builder;
	}
	
	private EzyAbstractHandlerGroup.Builder newBuilderByWebSocketType() {
		EzyAbstractHandlerGroup.Builder builder = EzySimpleWsHandlerGroup.builder();
		builder.sessionTicketsQueue(websocketSessionTicketsQueue);
		return builder;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyHandlerGroupBuilderFactory> {
		private EzySessionTicketsQueue socketSessionTicketsQueue;
		private EzySessionTicketsQueue websocketSessionTicketsQueue;
		
		public Builder socketSessionTicketsQueue(EzySessionTicketsQueue socketSessionTicketsQueue) {
			this.socketSessionTicketsQueue = socketSessionTicketsQueue;
			return this;
		}
		
		public Builder websocketSessionTicketsQueue(EzySessionTicketsQueue websocketSessionTicketsQueue) {
			this.websocketSessionTicketsQueue = websocketSessionTicketsQueue;
			return this;
		}
		
		@Override
		public EzyHandlerGroupBuilderFactory build() {
			return new EzyHandlerGroupBuilderFactoryImpl(this);
		}
	}
	
}
