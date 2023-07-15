package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public abstract class EzyAbstractSocketServerBootstrap implements EzyStartable, EzyDestroyable {

    protected EzyServer server;
    protected EzySettings serverSettings;
    protected EzyServerContext serverContext;
    protected EzySocketDataReceiver socketDataReceiver;
    protected EzyHandlerGroupManager handlerGroupManager;
    protected EzySessionTicketsQueue sessionTicketsQueue;
    protected EzySocketEventLoopHandler writingLoopHandler;

    public EzyAbstractSocketServerBootstrap(Builder<?, ?> builder) {
        this.serverContext = builder.serverContext;
        this.server = this.serverContext.getServer();
        this.serverSettings = this.server.getSettings();
        this.socketDataReceiver = builder.socketDataReceiver;
        this.handlerGroupManager = builder.handlerGroupManager;
        this.sessionTicketsQueue = builder.sessionTicketsQueue;
    }

    @Override
    public void destroy() {
        processWithLogException(() -> writingLoopHandler.destroy());
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<B, T extends EzyAbstractSocketServerBootstrap>
        implements EzyBuilder<T> {

        protected EzyServerContext serverContext;
        protected EzyHandlerGroupManager handlerGroupManager;
        protected EzySessionTicketsQueue sessionTicketsQueue;
        protected EzySocketDataReceiver socketDataReceiver;

        public B serverContext(EzyServerContext context) {
            this.serverContext = context;
            return (B) this;
        }

        public B socketDataReceiver(EzySocketDataReceiver socketDataReceiver) {
            this.socketDataReceiver = socketDataReceiver;
            return (B) this;
        }

        public B handlerGroupManager(EzyHandlerGroupManager manager) {
            this.handlerGroupManager = manager;
            return (B) this;
        }

        public B sessionTicketsQueue(EzySessionTicketsQueue queue) {
            this.sessionTicketsQueue = queue;
            return (B) this;
        }
    }
}
