package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.EzyHttpServerBootstrap;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyResponseApiAware;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.api.EzyStreamingApiAware;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import lombok.Setter;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;


public class EzyNioServerBootstrap extends EzyHttpServerBootstrap {

    @Setter
    private EzySslContextProxy sslContextProxy;
    @Setter
    private EzyResponseApi responseApi;
    @Setter
    private EzyStreamingApi streamingApi;
    @Setter
    private EzySocketStreamQueue streamQueue;
    @Setter
    private EzySocketDataReceiver socketDataReceiver;
    @Setter
    private EzyHandlerGroupManager handlerGroupManager;
    @Setter
    private EzySessionTicketsQueue socketSessionTicketsQueue;
    @Setter
    private EzySessionTicketsRequestQueues socketSessionTicketsRequestQueues;
    @Setter
    private EzySessionTicketsQueue websocketSessionTicketsQueue;
    @Setter
    private EzySocketDisconnectionQueue socketDisconnectionQueue;

    private EzyUdpServerBootstrap udpServerBootstrap;
    private EzySocketServerBootstrap socketServerBootstrap;
    private EzyWebSocketServerBootstrap websocketServerBootstrap;
    private EzySocketEventLoopOneHandler systemRequestHandlingLoopHandler;
    private EzySocketEventLoopOneHandler extensionRequestHandlingLoopHandler;
    private EzySocketEventLoopOneHandler streamHandlingLoopHandler;
    private EzySocketEventLoopOneHandler socketDisconnectionHandlingLoopHandler;
    private EzySocketEventLoopOneHandler socketUserRemovalHandlingLoopHandler;

    @Override
    protected void setupServer() {
        EzyServer server = getServer();
        ((EzyResponseApiAware) server).setResponseApi(responseApi);
        ((EzyStreamingApiAware) server).setStreamingApi(streamingApi);
    }

    @Override
    protected void startOtherBootstraps(Runnable callback) throws Exception {
        startSocketServerBootstrap();
        startUdpServerBootstrap();
        startWebSocketServerBootstrap();
        startRequestHandlingLoopHandlers();
        startStreamHandlingLoopHandlers();
        startDisconnectionHandlingLoopHandlers();
        startUserRemovalHandlingLoopHandlers();
        callback.run();
    }

    private void startSocketServerBootstrap() throws Exception {
        EzySocketSetting socketSetting = getSocketSetting();
        if (!socketSetting.isActive()) {
            return;
        }
        logger.debug("starting tcp socket server bootstrap ....");
        socketServerBootstrap = newSocketServerBootstrap();
        socketServerBootstrap.start();
        logger.debug("tcp socket server bootstrap has started");
    }

    private void startUdpServerBootstrap() throws Exception {
        EzyUdpSetting udpSetting = getUdpSetting();
        if (!udpSetting.isActive()) {
            return;
        }
        logger.debug("starting udp socket server bootstrap ....");
        udpServerBootstrap = newUdpServerBootstrap();
        udpServerBootstrap.start();
        logger.debug("udp socket server bootstrap has started");
    }

    protected void startWebSocketServerBootstrap() throws Exception {
        EzyWebSocketSetting wsSetting = getWebSocketSetting();
        if (!wsSetting.isActive()) {
            return;
        }
        logger.debug("starting websocket server bootstrap ....");
        websocketServerBootstrap = newWebSocketServerBootstrap();
        websocketServerBootstrap.start();
        logger.debug("websocket server bootstrap has started");
    }

    private void startRequestHandlingLoopHandlers() throws Exception {
        systemRequestHandlingLoopHandler = newSystemRequestHandlingLoopHandler();
        extensionRequestHandlingLoopHandler = newExtensionRequestHandlingLoopHandler();
        systemRequestHandlingLoopHandler.start();
        extensionRequestHandlingLoopHandler.start();
    }

    private void startStreamHandlingLoopHandlers() throws Exception {
        EzySettings settings = this.getServerSettings();
        EzyStreamingSetting streamingSetting = settings.getStreaming();
        if (streamingSetting.isEnable()) {
            streamHandlingLoopHandler = newSocketStreamHandlingLoopHandler();
            streamHandlingLoopHandler.start();
        }
    }

    private void startDisconnectionHandlingLoopHandlers() throws Exception {
        socketDisconnectionHandlingLoopHandler = newSocketDisconnectionHandlingLoopHandler();
        socketDisconnectionHandlingLoopHandler.start();
    }

    private void startUserRemovalHandlingLoopHandlers() throws Exception {
        socketUserRemovalHandlingLoopHandler = newSocketUserRemovalHandlingLoopHandler();
        socketUserRemovalHandlingLoopHandler.start();
    }

    private EzySocketServerBootstrap newSocketServerBootstrap() {
        return EzySocketServerBootstrap.builder()
            .serverContext(context)
            .sslContextProxy(sslContextProxy)
            .socketDataReceiver(socketDataReceiver)
            .handlerGroupManager(handlerGroupManager)
            .sessionTicketsQueue(socketSessionTicketsQueue)
            .build();
    }

    private EzyUdpServerBootstrap newUdpServerBootstrap() {
        return EzyUdpServerBootstrap.builder()
            .serverContext(context)
            .socketDataReceiver(socketDataReceiver)
            .handlerGroupManager(handlerGroupManager)
            .build();
    }

    private EzyWebSocketServerBootstrap newWebSocketServerBootstrap() {
        return EzyWebSocketServerBootstrap.builder()
            .serverContext(context)
            .sslContextProxy(sslContextProxy)
            .socketDataReceiver(socketDataReceiver)
            .handlerGroupManager(handlerGroupManager)
            .sessionTicketsQueue(websocketSessionTicketsQueue)
            .build();
    }

    private EzySocketEventLoopOneHandler newSystemRequestHandlingLoopHandler() {
        EzySocketEventLoopOneHandler loopHandler = new EzySocketSystemRequestHandlingLoopHandler();
        loopHandler.setThreadPoolSize(getSystemRequestHandlerPoolSize());
        EzySocketRequestHandler eventHandler = new EzySocketSystemRequestHandler();
        eventHandler.setDataHandlerGroupFetcher(handlerGroupManager);
        eventHandler.setSessionTicketsQueue(socketSessionTicketsRequestQueues.getSystemQueue());
        loopHandler.setEventHandler(eventHandler);
        return loopHandler;
    }

    private EzySocketEventLoopOneHandler newExtensionRequestHandlingLoopHandler() {
        EzySocketEventLoopOneHandler loopHandler = new EzySocketExtensionRequestHandlingLoopHandler();
        loopHandler.setThreadPoolSize(getExtensionRequestHandlerPoolSize());
        EzySocketRequestHandler eventHandler = new EzySocketExtensionRequestHandler();
        eventHandler.setDataHandlerGroupFetcher(handlerGroupManager);
        eventHandler.setSessionTicketsQueue(socketSessionTicketsRequestQueues.getExtensionQueue());
        loopHandler.setEventHandler(eventHandler);
        return loopHandler;
    }

    private EzySocketEventLoopOneHandler newSocketStreamHandlingLoopHandler() {
        EzySocketEventLoopOneHandler loopHandler = new EzySocketStreamHandlingLoopHandler();
        loopHandler.setThreadPoolSize(getStreamHandlerPoolSize());
        EzySocketStreamHandler eventHandler = new EzySocketStreamHandler();
        eventHandler.setStreamQueue(streamQueue);
        eventHandler.setDataHandlerGroupFetcher(handlerGroupManager);
        loopHandler.setEventHandler(eventHandler);
        return loopHandler;
    }

    private EzySocketEventLoopOneHandler newSocketDisconnectionHandlingLoopHandler() {
        EzySocketEventLoopOneHandler loopHandler = new EzySocketDisconnectionHandlingLoopHandler();
        loopHandler.setThreadPoolSize(getSocketDisconnectionHandlerPoolSize());
        EzySocketDisconnectionHandler eventHandler = new EzySocketDisconnectionHandler();
        eventHandler.setDataHandlerGroupRemover(handlerGroupManager);
        eventHandler.setDisconnectionQueue(socketDisconnectionQueue);
        loopHandler.setEventHandler(eventHandler);
        return loopHandler;
    }

    private EzySocketEventLoopOneHandler newSocketUserRemovalHandlingLoopHandler() {
        EzySocketEventLoopOneHandler loopHandler = new EzySocketUserRemovalHandlingLoopHandler();
        loopHandler.setThreadPoolSize(getSocketUserRemovalHandlerPoolSize());
        EzySocketUserRemovalQueue userRemovalQueue = context.get(EzySocketUserRemovalQueue.class);
        EzySocketUserRemovalHandler eventHandler = new EzySocketUserRemovalHandler(userRemovalQueue);
        loopHandler.setEventHandler(eventHandler);
        return loopHandler;
    }

    private int getStreamHandlerPoolSize() {
        return getThreadPoolSizeSetting().getStreamHandler();
    }

    private int getSystemRequestHandlerPoolSize() {
        return getThreadPoolSizeSetting().getSystemRequestHandler();
    }

    private int getExtensionRequestHandlerPoolSize() {
        return getThreadPoolSizeSetting().getExtensionRequestHandler();
    }

    private int getSocketDisconnectionHandlerPoolSize() {
        return getThreadPoolSizeSetting().getSocketDisconnectionHandler();
    }

    private int getSocketUserRemovalHandlerPoolSize() {
        return getThreadPoolSizeSetting().getSocketUserRemovalHandler();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (socketServerBootstrap != null) {
            processWithLogException(socketServerBootstrap::destroy);
        }
        if (websocketServerBootstrap != null) {
            processWithLogException(websocketServerBootstrap::destroy);
        }
        if (udpServerBootstrap != null) {
            processWithLogException(udpServerBootstrap::destroy);
        }
        if (socketDataReceiver != null) {
            processWithLogException(socketDataReceiver::destroy);
        }
        if (handlerGroupManager != null) {
            processWithLogException(handlerGroupManager::destroy);
        }
        if (streamHandlingLoopHandler != null) {
            processWithLogException(streamHandlingLoopHandler::destroy);
        }
        if (systemRequestHandlingLoopHandler != null) {
            processWithLogException(systemRequestHandlingLoopHandler::destroy);
        }
        if (extensionRequestHandlingLoopHandler != null) {
            processWithLogException(extensionRequestHandlingLoopHandler::destroy);
        }
        if (socketDisconnectionHandlingLoopHandler != null) {
            processWithLogException(socketDisconnectionHandlingLoopHandler::destroy);
        }
        if (socketUserRemovalHandlingLoopHandler != null) {
            processWithLogException(socketUserRemovalHandlingLoopHandler::destroy);
        }
        this.socketServerBootstrap = null;
        this.websocketServerBootstrap = null;
        this.socketDataReceiver = null;
        this.handlerGroupManager = null;
        this.streamHandlingLoopHandler = null;
        this.systemRequestHandlingLoopHandler = null;
        this.extensionRequestHandlingLoopHandler = null;
        this.socketDisconnectionHandlingLoopHandler = null;
        this.socketUserRemovalHandlingLoopHandler = null;
    }
}
