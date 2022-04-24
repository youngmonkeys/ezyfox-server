package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.EzyBootstrap;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.ezyfoxserver.ssl.EzySslContextInitializer;

import javax.net.ssl.SSLContext;

public abstract class EzyAbstractServerBootstrapBuilder
    implements EzyServerBootstrapBuilder {

    protected EzyServer server;
    protected EzyServerContext serverContext;

    @Override
    public EzyAbstractServerBootstrapBuilder server(EzyServer server) {
        this.server = server;
        this.serverContext = newServerContext(server);
        return this;
    }

    @Override
    public final EzyServerBootstrap build() {
        preBuild();
        return buildServerBootstrap();
    }

    protected void preBuild() {}

    protected EzyServerBootstrap buildServerBootstrap() {
        EzyServerBootstrap answer = newServerBootstrap();
        answer.setContext(serverContext);
        answer.setLocalBootstrap(newLocalBoostrap());
        return answer;
    }

    protected EzyServerContext newServerContext(EzyServer server) {
        return newServerContextBuilder()
            .server(server)
            .build();
    }

    protected SSLContext newSslContext(EzySslConfigSetting sslConfig) {
        if (getWebsocketSetting().isSslActive()) {
            return newSslContextInitializer(sslConfig).init();
        }
        return null;
    }

    protected EzySslContextInitializer newSslContextInitializer(EzySslConfigSetting sslConfig) {
        return newSslContextInitializerBuilder()
            .sslConfig(sslConfig)
            .homeFolderPath(getConfig().getEzyfoxHome())
            .build();
    }

    protected EzySslContextInitializer.Builder newSslContextInitializerBuilder() {
        return EzySslContextInitializer.builder();
    }

    protected EzyBootstrap newLocalBoostrap() {
        return EzyBootstrap.builder().context(serverContext).build();
    }

    protected EzyServerContextBuilder<?> newServerContextBuilder() {
        return new EzySimpleServerContextBuilder<>();
    }

    protected abstract EzyServerBootstrap newServerBootstrap();

    protected EzyConfig getConfig() {
        return server.getConfig();
    }

    protected EzySettings getSettings() {
        return server.getSettings();
    }

    protected EzySocketSetting getSocketSetting() {
        return getSettings().getSocket();
    }

    protected EzyWebSocketSetting getWebsocketSetting() {
        return getSettings().getWebsocket();
    }

    protected EzyThreadPoolSizeSetting getThreadPoolSizeSetting() {
        return getSettings().getThreadPoolSize();
    }
}


