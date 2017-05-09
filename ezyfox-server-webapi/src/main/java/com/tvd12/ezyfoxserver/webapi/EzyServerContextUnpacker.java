package com.tvd12.ezyfoxserver.webapi;

import org.springframework.beans.factory.annotation.Autowired;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyServerUserManager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyServerContextUnpacker extends EzyLoggable {

	@Autowired
	protected EzyServerContext serverContext;
	
	protected EzyServer getServer() {
		return serverContext.getServer();
	}
	
	protected EzyConfig getConfig() {
		return getServer().getConfig();
	}
	
	protected EzySettings getSettings() {
		return getServer().getSettings();
	}
	
	protected EzyManagers getServerManagers() {
		return getServer().getManagers();
	}
	
	protected EzyAppContext getAppContext(String appName) {
		return serverContext.getAppContext(appName);
	}
	
	protected EzyServerUserManager getServerUserManager() {
		return getServerManagers().getManager(EzyServerUserManager.class);
	}
	
	@SuppressWarnings("unchecked")
	protected EzySessionManager<EzySession> getServerSessionManger() {
		return getServerManagers().getManager(EzySessionManager.class);
	}
	
}
