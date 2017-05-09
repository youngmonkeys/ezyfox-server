package com.tvd12.ezyfoxserver.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.EzyHttpSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;

@Component
public class EzyEmbeddedServletContainerCustomizer 
		implements EmbeddedServletContainerCustomizer {
	
	@Autowired
	protected EzyServerContext serverContext;
	
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		EzyServer server = serverContext.getServer();
		EzySettings settings = server.getSettings();
		EzyHttpSetting http = settings.getHttp();
		container.setPort(http.getPort());
	}
}
