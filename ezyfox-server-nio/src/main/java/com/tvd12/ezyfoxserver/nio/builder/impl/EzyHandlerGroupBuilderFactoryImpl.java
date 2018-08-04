package com.tvd12.ezyfoxserver.nio.builder.impl;

import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzySimpleNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.websocket.EzySimpleWsHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkStats;
import com.tvd12.ezyfoxserver.statistics.EzySessionStats;
import com.tvd12.ezyfoxserver.statistics.EzySocketStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyWebSocketStatistics;

public class EzyHandlerGroupBuilderFactoryImpl implements EzyHandlerGroupBuilderFactory {

	protected EzyStatistics statistics;
	protected AtomicInteger socketSessionCount;
	protected AtomicInteger webSocketSessionCount;
	protected EzySessionTicketsQueue socketSessionTicketsQueue;
	protected EzySessionTicketsQueue webSocketSessionTicketsQueue;
	
	public EzyHandlerGroupBuilderFactoryImpl(Builder builder) {
		this.statistics = builder.statistics;
		this.socketSessionCount = new AtomicInteger(0);
		this.webSocketSessionCount = new AtomicInteger(0);
		this.socketSessionTicketsQueue = builder.socketSessionTicketsQueue;
		this.webSocketSessionTicketsQueue = builder.webSocketSessionTicketsQueue;
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
		builder.sessionCount(socketSessionCount);
		builder.sessionStats(getSocketSessionStats());
		builder.networkStats(getSocketNetworkStats());
		builder.sessionTicketsQueue(socketSessionTicketsQueue);
		return builder;
	}
	
	private EzyAbstractHandlerGroup.Builder newBuilderByWebSocketType() {
		EzyAbstractHandlerGroup.Builder builder = EzySimpleWsHandlerGroup.builder();
		builder.sessionCount(webSocketSessionCount);
		builder.sessionStats(getWebSocketSessionStats());
		builder.networkStats(getWebSocketNetworkStats());
		builder.sessionTicketsQueue(webSocketSessionTicketsQueue);
		return builder;
	}
	
	private EzySessionStats getSocketSessionStats() {
		return (EzySessionStats) getSocketStatistics().getSessionStats();
	}
	
	private EzySessionStats getWebSocketSessionStats() {
		return (EzySessionStats) getWebSocketStatistics().getSessionStats();
	}
	
	private EzyNetworkStats getSocketNetworkStats() {
		return (EzyNetworkStats) getSocketStatistics().getNetworkStats();
	}
	
	private EzyNetworkStats getWebSocketNetworkStats() {
		return (EzyNetworkStats) getWebSocketStatistics().getNetworkStats();
	}
	
	private EzySocketStatistics getSocketStatistics() {
		return statistics.getSocketStats();
	}
	
	private EzyWebSocketStatistics getWebSocketStatistics() {
		return statistics.getWebSocketStats();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyHandlerGroupBuilderFactory> {
		protected EzyStatistics statistics;
		private EzySessionTicketsQueue socketSessionTicketsQueue;
		private EzySessionTicketsQueue webSocketSessionTicketsQueue;
		
		public Builder statistics(EzyStatistics statistics) {
			this.statistics = statistics;
			return this;
		}
		
		public Builder socketSessionTicketsQueue(EzySessionTicketsQueue socketSessionTicketsQueue) {
			this.socketSessionTicketsQueue = socketSessionTicketsQueue;
			return this;
		}
		
		public Builder webSocketSessionTicketsQueue(EzySessionTicketsQueue webSocketSessionTicketsQueue) {
			this.webSocketSessionTicketsQueue = webSocketSessionTicketsQueue;
			return this;
		}
		
		@Override
		public EzyHandlerGroupBuilderFactory build() {
			return new EzyHandlerGroupBuilderFactoryImpl(this);
		}
	}
	
}
