package com.tvd12.ezyfoxserver.nio.protocol;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.reflect.EzyClasses;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolGateway;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolServer;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolServerContext;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolServerCreator;
import com.tvd12.ezyfoxserver.setting.EzyProtocolSetting;
import com.tvd12.ezyfoxserver.setting.EzyProtocolsSetting;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketWriter;
import com.tvd12.ezyfoxserver.socket.EzySocketWritingLoopHandler;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;

import java.util.ArrayList;
import java.util.List;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzyProtocolServersBootstrap
    extends EzyLoggable
    implements EzyStartable, EzyDestroyable {

    private final EzyServerContext serverContext;
    private final EzyProtocolGateway protocolGateway;
    private final EzySslContextProxy sslContextProxy;
    private final EzyProtocolsSetting protocolsSetting;
    private final EzyHandlerGroupManager handlerGroupManager;
    private final EzySessionTicketsQueue sessionTicketsQueue;
    private final List<EzyProtocolServer> protocolServers;
    private EzySocketEventLoopHandler writingLoopHandler;

    public EzyProtocolServersBootstrap(Builder builder) {
        this.serverContext = builder.serverContext;
        this.protocolGateway = builder.protocolGateway;
        this.sslContextProxy = builder.sslContextProxy;
        this.handlerGroupManager = builder.handlerGroupManager;
        this.sessionTicketsQueue = builder.sessionTicketsQueue;
        this.protocolsSetting = serverContext
            .getServer()
            .getSettings()
            .getProtocols();
        this.protocolServers = new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void start() throws Exception {
        writingLoopHandler = newWritingLoopHandler();
        writingLoopHandler.start();
        for (EzyProtocolSetting setting : protocolsSetting.getActiveProtocols()) {
            try {
                startProtocolServer(setting);
            } catch (Throwable e) {
                logger.error(
                    "start protocol: {} server failed",
                    setting.getName(),
                    e
                );
            }
        }
    }

    private void startProtocolServer(
        EzyProtocolSetting setting
    ) throws Exception {
        logger.debug("starting protocol: {} server ....", setting.getName());
        EzyProtocolServerCreator serverCreator = newServerCreator(setting);
        EzyProtocolServer protocolServer = serverCreator.create(
            new EzyProtocolServerContext(
                setting,
                protocolGateway,
                serverContext,
                sslContextProxy
            )
        );
        protocolServer.start();
        protocolServers.add(protocolServer);
        logger.debug(
            "protocol: {} server has started at: {}:{}",
            setting.getName(),
            setting.getAddress(),
            setting.getPort()
        );
    }

    private EzyProtocolServerCreator newServerCreator(
        EzyProtocolSetting setting
    ) {
        String serverCreator = setting.getServerCreator();
        if (serverCreator == null) {
            throw new IllegalArgumentException(
                "server-creator of protocol: " + setting.getName() + " is required"
            );
        }
        ClassLoader classLoader = serverContext.getServer().getClassLoader();
        return classLoader != null
            ? EzyClasses.newInstance(serverCreator, classLoader)
            : EzyClasses.newInstance(serverCreator);
    }

    private EzySocketEventLoopHandler newWritingLoopHandler() {
        EzySocketWritingLoopHandler loopHandler = new EzyProtocolWritingLoopHandler();
        loopHandler.setThreadPoolSize(protocolsSetting.getWriterThreadPoolSize());
        loopHandler.setEventHandlerSupplier(() -> {
            EzySocketWriter eventHandler = new EzySocketWriter();
            eventHandler.setWriterGroupFetcher(handlerGroupManager);
            eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
            return eventHandler;
        });
        return loopHandler;
    }

    @Override
    public void destroy() {
        for (EzyProtocolServer protocolServer : protocolServers) {
            processWithLogException(protocolServer::destroy);
        }
        protocolServers.clear();
        if (writingLoopHandler != null) {
            processWithLogException(writingLoopHandler::destroy);
        }
    }

    public static class Builder implements EzyBuilder<EzyProtocolServersBootstrap> {

        private EzyServerContext serverContext;
        private EzyProtocolGateway protocolGateway;
        private EzySslContextProxy sslContextProxy;
        private EzyHandlerGroupManager handlerGroupManager;
        private EzySessionTicketsQueue sessionTicketsQueue;

        public Builder serverContext(EzyServerContext serverContext) {
            this.serverContext = serverContext;
            return this;
        }

        public Builder protocolGateway(EzyProtocolGateway protocolGateway) {
            this.protocolGateway = protocolGateway;
            return this;
        }

        public Builder sslContextProxy(EzySslContextProxy sslContextProxy) {
            this.sslContextProxy = sslContextProxy;
            return this;
        }

        public Builder handlerGroupManager(EzyHandlerGroupManager handlerGroupManager) {
            this.handlerGroupManager = handlerGroupManager;
            return this;
        }

        public Builder sessionTicketsQueue(EzySessionTicketsQueue sessionTicketsQueue) {
            this.sessionTicketsQueue = sessionTicketsQueue;
            return this;
        }

        @Override
        public EzyProtocolServersBootstrap build() {
            return new EzyProtocolServersBootstrap(this);
        }
    }
}
