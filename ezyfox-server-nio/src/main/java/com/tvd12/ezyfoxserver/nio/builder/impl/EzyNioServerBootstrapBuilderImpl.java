package com.tvd12.ezyfoxserver.nio.builder.impl;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.api.*;
import com.tvd12.ezyfoxserver.builder.EzyHttpServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.codec.EzySimpleCodecFactory;
import com.tvd12.ezyfoxserver.nio.EzyNioServerBootstrap;
import com.tvd12.ezyfoxserver.nio.builder.EzyNioServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.socket.EzySecureSocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyHandlerGroupManagerImpl;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;

import java.util.concurrent.ExecutorService;

public class EzyNioServerBootstrapBuilderImpl
    extends EzyHttpServerBootstrapBuilder
    implements EzyNioServerBootstrapBuilder {

    @Override
    protected EzyServerBootstrap newServerBootstrap() {
        ExecutorService statsThreadPool = newStatsThreadPool();
        EzyCodecFactory codecFactory = newCodecFactory();
        EzyStreamingApi streamingApi = newStreamingApi();
        EzyResponseApi responseApi = newResponseApi(codecFactory);
        EzySocketStreamQueue streamQueue = newStreamQueue();
        EzySessionTicketsQueue socketSessionTicketsQueue = newSocketSessionTicketsQueue();
        EzySessionTicketsQueue websocketSessionTicketsQueue = newWebSocketSessionTicketsQueue();
        EzySocketDisconnectionQueue socketDisconnectionQueue = newSocketDisconnectionQueue();
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = newSessionTicketsRequestQueues();
        EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = newHandlerGroupBuilderFactory(
            statsThreadPool,
            codecFactory,
            streamQueue,
            socketDisconnectionQueue,
            socketSessionTicketsQueue,
            websocketSessionTicketsQueue,
            sessionTicketsRequestQueues
        );
        EzyHandlerGroupManager handlerGroupManager = newHandlerGroupManager(
            handlerGroupBuilderFactory
        );
        EzySocketDataReceiver socketDataReceiver = newSocketDataReceiver(
            handlerGroupManager
        );
        EzySslContextProxy sslContextProxy = new EzySslContextProxy(
            () -> newSslContext(getWebsocketSetting().getSslConfig())
        );
        serverContext.setProperty(EzySslContextProxy.class, sslContextProxy);
        EzyNioServerBootstrap bootstrap = new EzyNioServerBootstrap();
        bootstrap.setResponseApi(responseApi);
        bootstrap.setStreamingApi(streamingApi);
        bootstrap.setStreamQueue(streamQueue);
        bootstrap.setSocketDataReceiver(socketDataReceiver);
        bootstrap.setHandlerGroupManager(handlerGroupManager);
        bootstrap.setSocketDisconnectionQueue(socketDisconnectionQueue);
        bootstrap.setSocketSessionTicketsQueue(socketSessionTicketsQueue);
        bootstrap.setWebsocketSessionTicketsQueue(websocketSessionTicketsQueue);
        bootstrap.setSocketSessionTicketsRequestQueues(sessionTicketsRequestQueues);
        bootstrap.setSslContextProxy(sslContextProxy);
        return bootstrap;
    }

    private EzyHandlerGroupManager newHandlerGroupManager(
        EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory) {

        return EzyHandlerGroupManagerImpl.builder()
            .handlerGroupBuilderFactory(handlerGroupBuilderFactory)
            .build();
    }

    private EzyHandlerGroupBuilderFactory newHandlerGroupBuilderFactory(
        ExecutorService statsThreadPool,
        EzyCodecFactory codecFactory,
        EzySocketStreamQueue streamQueue,
        EzySocketDisconnectionQueue disconnectionQueue,
        EzySessionTicketsQueue socketSessionTicketsQueue,
        EzySessionTicketsQueue websocketSessionTicketsQueue,
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues) {

        return EzyHandlerGroupBuilderFactoryImpl.builder()
            .statistics(server.getStatistics())
            .serverContext(serverContext)
            .streamQueue(streamQueue)
            .codecFactory(codecFactory)
            .statsThreadPool(statsThreadPool)
            .disconnectionQueue(disconnectionQueue)
            .socketSessionTicketsQueue(socketSessionTicketsQueue)
            .webSocketSessionTicketsQueue(websocketSessionTicketsQueue)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
    }

    protected EzyStreamingApi newStreamingApi() {
        return new EzyProxyStreamingApi();
    }

    protected EzyResponseApi newResponseApi(EzyCodecFactory codecFactory) {
        EzySocketSetting socketSetting = getSocketSetting();
        return socketSetting.isCertificationSslActive()
            ? new EzySecureProxyResponseApi(codecFactory)
            : new EzyProxyResponseApi(codecFactory);
    }

    private ExecutorService newStatsThreadPool() {
        int threadPoolSize = getThreadPoolSizeSetting().getStatistics();
        return EzyExecutors.newFixedThreadPool(threadPoolSize, "statistics");
    }

    private EzySocketDataReceiver newSocketDataReceiver(
        EzyHandlerGroupManager handlerGroupManager
    ) {
        return newSocketDataReceiverBuilder()
            .handlerGroupManager(handlerGroupManager)
            .threadPoolSize(getThreadPoolSizeSetting().getSocketDataReceiver())
            .build();
    }

    private EzySocketDataReceiver.Builder newSocketDataReceiverBuilder() {
        EzySocketSetting setting = getSocketSetting();
        return setting.isCertificationSslActive()
            ? EzySecureSocketDataReceiver.builder()
            : EzySocketDataReceiver.builder();
    }

    private EzySocketStreamQueue newStreamQueue() {
        return new EzyBlockingSocketStreamQueue();
    }

    private EzySessionTicketsQueue newSocketSessionTicketsQueue() {
        return new EzyBlockingSessionTicketsQueue();
    }

    private EzySessionTicketsQueue newWebSocketSessionTicketsQueue() {
        return new EzyBlockingSessionTicketsQueue();
    }

    private EzySocketDisconnectionQueue newSocketDisconnectionQueue() {
        return new EzyBlockingSocketDisconnectionQueue();
    }

    private EzySessionTicketsRequestQueues newSessionTicketsRequestQueues() {
        return new EzySessionTicketsRequestQueues();
    }

    private EzyCodecFactory newCodecFactory() {
        return EzySimpleCodecFactory.builder()
            .socketSetting(getSocketSetting())
            .websocketSetting(getWebsocketSetting())
            .build();
    }
}
