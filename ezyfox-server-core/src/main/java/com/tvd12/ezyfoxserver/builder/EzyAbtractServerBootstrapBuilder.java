package com.tvd12.ezyfoxserver.builder;

import javax.net.ssl.SSLContext;

import com.tvd12.ezyfoxserver.EzyBootstrap;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySslConfigSetting;
import com.tvd12.ezyfoxserver.setting.EzyThreadPoolSizeSetting;
import com.tvd12.ezyfoxserver.setting.EzyUdpSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.ssl.EzySslContextInitializer;

public abstract class EzyAbtractServerBootstrapBuilder 
		implements EzyServerBootstrapBuilder {

	protected EzyServer server;
	protected EzyServerContext serverContext;
	
	@Override
	public EzyAbtractServerBootstrapBuilder server(EzyServer server) {
		this.server = server;
		this.serverContext = newServerContext(server);
		return this;
	}
	
	@Override
	public final EzyServerBootstrap build() {
	    prebuild();
        return buildServerBootstrap();
	}
	
	protected void prebuild() {
	}
	
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
	    return newSslContextInitializer(sslConfig).init();
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
    
    protected EzyUdpSetting getUdpSetting() {
        return getSettings().getUdp();
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


