package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.constant.EzyNioThreadPoolSizes;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioUdpDataHandler;
import com.tvd12.ezyfoxserver.nio.handler.EzySimpleNioUdpDataHandler;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.udp.EzyNioUdpReader;
import com.tvd12.ezyfoxserver.nio.udp.EzyNioUdpReadingLoopHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzyUdpSetting;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPool;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopOneHandler;

import java.net.InetSocketAddress;
import java.nio.channels.Selector;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzyUdpServerBootstrap implements EzyStartable, EzyDestroyable {

    private Selector readSelector;
    private final EzyServer server;
    private final EzyUdpSetting udpSetting;
    private final EzySocketDataReceiver socketDataReceiver;
    private final EzyHandlerGroupManager handlerGroupManager;
    private final EzyDatagramChannelPool udpChannelPool;
    private final EzyNioUdpDataHandler udpDataHandler;
    private EzySocketEventLoopHandler readingLoopHandler;

    public EzyUdpServerBootstrap(Builder builder) {
        EzyServerContext serverContext = builder.serverContext;
        this.server = serverContext.getServer();
        this.udpSetting = server.getSettings().getUdp();
        this.socketDataReceiver = builder.socketDataReceiver;
        this.handlerGroupManager = builder.handlerGroupManager;
        this.udpChannelPool = newChannelPool();
        this.udpDataHandler = newUdpDataHandler();
        serverContext.setProperty(EzyNioUdpDataHandler.class, udpDataHandler);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void start() throws Exception {
        openSelectors();
        configChannelPool();
        startSocketHandlers();
    }

    @Override
    public void destroy() {
        processWithLogException(readingLoopHandler::destroy);
        processWithLogException(udpChannelPool::close);
    }

    private void openSelectors() throws Exception {
        this.readSelector = openSelector();
    }

    private EzyDatagramChannelPool newChannelPool() {
        return new EzyDatagramChannelPool(
            udpSetting.getChannelPoolSize()
        );
    }

    private void configChannelPool() {
        this.udpChannelPool.bind(
            new InetSocketAddress(
                udpSetting.getAddress(),
                udpSetting.getPort()
            )
        );
        this.udpChannelPool.register(readSelector);
    }

    private void startSocketHandlers() throws Exception {
        readingLoopHandler = newReadingLoopHandler();
        readingLoopHandler.start();
    }

    private EzySocketEventLoopHandler newReadingLoopHandler() {
        EzySocketEventLoopOneHandler loopHandler = new EzyNioUdpReadingLoopHandler();
        loopHandler.setThreadPoolSize(getSocketReaderPoolSize());
        EzyNioUdpReader eventHandler = new EzyNioUdpReader(
            udpSetting.getMaxRequestSize()
        );
        eventHandler.setOwnSelector(readSelector);
        eventHandler.setUdpDataHandler(udpDataHandler);
        loopHandler.setEventHandler(eventHandler);
        return loopHandler;
    }

    private EzyNioUdpDataHandler newUdpDataHandler() {
        EzySimpleNioUdpDataHandler handler = new EzySimpleNioUdpDataHandler(
            udpSetting.getHandlerThreadPoolSize()
        );
        handler.setResponseApi(server.getResponseApi());
        handler.setDatagramChannelPool(udpChannelPool);
        handler.setSessionManager(server.getSessionManager());
        handler.setSocketDataReceiver(socketDataReceiver);
        handler.setHandlerGroupManager(handlerGroupManager);
        return handler;
    }

    private Selector openSelector() throws Exception {
        return Selector.open();
    }

    private int getSocketReaderPoolSize() {
        return EzyNioThreadPoolSizes.SOCKET_READER;
    }

    public static class Builder implements EzyBuilder<EzyUdpServerBootstrap> {

        private EzyServerContext serverContext;
        private EzySocketDataReceiver socketDataReceiver;
        private EzyHandlerGroupManager handlerGroupManager;

        public Builder serverContext(EzyServerContext context) {
            this.serverContext = context;
            return this;
        }

        public Builder socketDataReceiver(EzySocketDataReceiver socketDataReceiver) {
            this.socketDataReceiver = socketDataReceiver;
            return this;
        }

        public Builder handlerGroupManager(EzyHandlerGroupManager manager) {
            this.handlerGroupManager = manager;
            return this;
        }

        @Override
        public EzyUdpServerBootstrap build() {
            return new EzyUdpServerBootstrap(this);
        }
    }
}
