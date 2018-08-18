package com.tvd12.ezyfoxserver;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerReadyEvent;
import com.tvd12.ezyfoxserver.setting.EzyHttpSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.util.EzyBannerPrinter;

import lombok.Setter;


public abstract class EzyServerBootstrap
        extends EzyLoggable
        implements EzyStartable, EzyDestroyable {
	
	@Setter
	protected EzyServerContext context;
	@Setter
	protected EzyBootstrap localBootstrap;
	
	@Override
	public void start() throws Exception {
	    setupServer();
		startLocalBootstrap();
		startHttpBootstrap();
		startOtherBootstraps(this::notifyServerReady);
	}
	
	protected void setupServer() {
	}
	
	protected void startHttpBootstrap() throws Exception {
	}
	
	protected abstract void startOtherBootstraps(Runnable callback) throws Exception;
	
	@Override
	public void destroy() {
		processWithLogException(localBootstrap::destroy);
	}
	
	protected void startLocalBootstrap() throws Exception {
		getLogger().debug("starting local bootstrap ....");
		localBootstrap.start();
		getLogger().debug("local bootstrap has started");
	}
	
	protected void notifyServerReady() {
	    getLogger().info("\n{}\n", new EzyBannerPrinter().getBannerString());
		context.get(EzyFireEvent.class).fire(EzyEventType.SERVER_READY, newServerReadyEvent());
	}
	
	protected EzyEvent newServerReadyEvent() {
		return new EzySimpleServerReadyEvent();
	}
	
	protected EzyServer getServer() {
	    return context.getServer();
	}
	
	protected EzySettings getServerSettings() {
	    return getServer().getSettings();
	}
	
	protected EzyHttpSetting getHttpSetting() {
	    return getServerSettings().getHttp();
	}
	
	protected EzySocketSetting getSocketSetting() {
        return getServerSettings().getSocket();
    }
	
	protected EzyWebSocketSetting getWebSocketSetting() {
        return getServerSettings().getWebsocket();
    }
	
}
