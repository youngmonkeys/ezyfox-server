package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketSecureServerCreator;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketServerCreator;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsWritingLoopHandler;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketWriter;
import org.eclipse.jetty.server.Server;

import javax.net.ssl.SSLContext;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzyWebSocketServerBootstrap extends EzyAbstractSocketServerBootstrap {

    private Server server;
    private final SSLContext sslContext;

    public EzyWebSocketServerBootstrap(Builder builder) {
        super(builder);
        this.sslContext = builder.sslContext;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void start() throws Exception {
        server = newSocketServer();
        server.start();
        writingLoopHandler = newWritingLoopHandler();
        writingLoopHandler.start();
    }

    @Override
    public void destroy() {
        processWithLogException(() -> writingLoopHandler.destroy());
        processWithLogException(() -> server.stop());
    }

    private Server newSocketServer() {
        return newSocketServerCreator()
            .setting(getWsSetting())
            .sessionManager(getSessionManager())
            .handlerGroupManager(handlerGroupManager)
            .sessionManagementSetting(getSessionManagementSetting())
            .socketDataReceiver(socketDataReceiver)
            .create();
    }

    private EzySocketEventLoopHandler newWritingLoopHandler() {
        EzyWsWritingLoopHandler loopHandler = new EzyWsWritingLoopHandler();
        loopHandler.setThreadPoolSize(getSocketWriterPoolSize());
        loopHandler.setEventHandlerSupplier(() -> {
            EzySocketWriter eventHandler = new EzySocketWriter();
            eventHandler.setWriterGroupFetcher(handlerGroupManager);
            eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
            return eventHandler;
        });
        return loopHandler;
    }

    private EzyWebSocketServerCreator newSocketServerCreator() {
        if (isSslActive()) {
            return new EzyWebSocketSecureServerCreator(sslContext);
        }
        return new EzyWebSocketServerCreator();
    }

    private int getSocketWriterPoolSize() {
        return getWsSetting().getWriterThreadPoolSize();
    }

    private boolean isSslActive() {
        return getWsSetting().isSslActive();
    }

    private EzyWebSocketSetting getWsSetting() {
        return getServerSettings().getWebsocket();
    }

    public static class Builder
        extends EzyAbstractSocketServerBootstrap.Builder<Builder, EzyWebSocketServerBootstrap> {

        private SSLContext sslContext;

        public Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        @Override
        public EzyWebSocketServerBootstrap build() {
            return new EzyWebSocketServerBootstrap(this);
        }
    }
}
