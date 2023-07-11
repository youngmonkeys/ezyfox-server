package com.tvd12.ezyfoxserver.nio.builder.impl;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.creator.EzySessionCreator;
import com.tvd12.ezyfoxserver.creator.EzySimpleSessionCreator;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzySimpleNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.websocket.EzySimpleWsHandlerGroup;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.ezyfoxserver.statistics.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class EzyHandlerGroupBuilderFactoryImpl implements EzyHandlerGroupBuilderFactory {

    private final ExecutorService statsThreadPool;

    private final EzyStatistics statistics;
    private final AtomicInteger socketSessionCount;
    private final AtomicInteger webSocketSessionCount;
    private final EzyCodecFactory codecFactory;
    private final EzyServerContext serverContext;
    private final EzySessionCreator sessionCreator;
    private final EzySocketStreamQueue streamQueue;
    private final EzySocketDisconnectionQueue disconnectionQueue;
    private final EzySessionTicketsQueue socketSessionTicketsQueue;
    private final EzySessionTicketsQueue webSocketSessionTicketsQueue;
    private final EzySessionTicketsRequestQueues sessionTicketsRequestQueues;

    public EzyHandlerGroupBuilderFactoryImpl(Builder builder) {
        this.socketSessionCount = new AtomicInteger(0);
        this.webSocketSessionCount = new AtomicInteger(0);
        this.statistics = builder.statistics;
        this.statsThreadPool = builder.statsThreadPool;
        this.streamQueue = builder.streamQueue;
        this.codecFactory = builder.codecFactory;
        this.serverContext = builder.serverContext;
        this.sessionCreator = builder.sessionCreator;
        this.disconnectionQueue = builder.disconnectionQueue;
        this.socketSessionTicketsQueue = builder.socketSessionTicketsQueue;
        this.webSocketSessionTicketsQueue = builder.webSocketSessionTicketsQueue;
        this.sessionTicketsRequestQueues = builder.sessionTicketsRequestQueues;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public EzyAbstractHandlerGroup.Builder newBuilder(
        EzyChannel channel,
        EzyConnectionType type
    ) {
        EzyAbstractHandlerGroup.Builder builder = (type == EzyConnectionType.SOCKET)
            ? newBuilderBySocketType()
            : newBuilderByWebSocketType();
        builder.session(sessionCreator.create(channel));
        builder.serverContext(serverContext);
        builder.streamQueue(streamQueue);
        builder.disconnectionQueue(disconnectionQueue);
        builder.decoder(newDataDecoder(type));
        builder.statsThreadPool(statsThreadPool);
        builder.sessionTicketsRequestQueues(sessionTicketsRequestQueues);
        return builder;
    }

    private EzyAbstractHandlerGroup.Builder newBuilderBySocketType() {
        return EzySimpleNioHandlerGroup.builder()
            .sessionCount(socketSessionCount)
            .sessionStats(getSocketSessionStats())
            .networkStats(getSocketNetworkStats())
            .sessionTicketsQueue(socketSessionTicketsQueue);
    }

    private EzyAbstractHandlerGroup.Builder newBuilderByWebSocketType() {
        EzyAbstractHandlerGroup.Builder builder = EzySimpleWsHandlerGroup.builder();
        builder.sessionCount(webSocketSessionCount);
        builder.sessionStats(getWebSocketSessionStats());
        builder.networkStats(getWebSocketNetworkStats());
        builder.sessionTicketsQueue(webSocketSessionTicketsQueue);
        return builder;
    }

    private Object newDataDecoder(EzyConnectionType type) {
        return codecFactory.newDecoder(type);
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

    public static class Builder implements EzyBuilder<EzyHandlerGroupBuilderFactory> {
        private EzyStatistics statistics;
        private ExecutorService statsThreadPool;
        private EzyCodecFactory codecFactory;
        private EzyServerContext serverContext;
        private EzySessionCreator sessionCreator;
        private EzySocketStreamQueue streamQueue;
        private EzySocketDisconnectionQueue disconnectionQueue;
        private EzySessionTicketsQueue socketSessionTicketsQueue;
        private EzySessionTicketsQueue webSocketSessionTicketsQueue;
        private EzySessionTicketsRequestQueues sessionTicketsRequestQueues;

        public Builder statistics(EzyStatistics statistics) {
            this.statistics = statistics;
            return this;
        }

        public Builder statsThreadPool(ExecutorService statsThreadPool) {
            this.statsThreadPool = statsThreadPool;
            return this;
        }

        public Builder streamQueue(EzySocketStreamQueue streamQueue) {
            this.streamQueue = streamQueue;
            return this;
        }

        public Builder disconnectionQueue(EzySocketDisconnectionQueue disconnectionQueue) {
            this.disconnectionQueue = disconnectionQueue;
            return this;
        }

        public Builder codecFactory(EzyCodecFactory codecFactory) {
            this.codecFactory = codecFactory;
            return this;
        }

        public Builder serverContext(EzyServerContext serverContext) {
            this.serverContext = serverContext;
            return this;
        }

        public Builder sessionCreator(EzySessionCreator sessionCreator) {
            this.sessionCreator = sessionCreator;
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

        public Builder sessionTicketsRequestQueues(EzySessionTicketsRequestQueues sessionTicketsRequestQueues) {
            this.sessionTicketsRequestQueues = sessionTicketsRequestQueues;
            return this;
        }

        @Override
        public EzyHandlerGroupBuilderFactory build() {
            if (sessionCreator == null) {
                this.sessionCreator = newSessionCreator(serverContext);
            }
            return new EzyHandlerGroupBuilderFactoryImpl(this);
        }

        protected EzySessionCreator newSessionCreator(EzyServerContext serverContext) {
            EzyServer server = serverContext.getServer();
            EzySettings settings = server.getSettings();
            return EzySimpleSessionCreator.builder()
                .sessionManager(server.getSessionManager())
                .sessionSetting(settings.getSessionManagement())
                .build();
        }
    }
}
