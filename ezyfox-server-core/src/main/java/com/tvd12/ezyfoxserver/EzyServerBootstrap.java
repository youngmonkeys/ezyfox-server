package com.tvd12.ezyfoxserver;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
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
		logger.debug("starting local bootstrap ....");
		localBootstrap.start();
		logger.debug("local bootstrap has started");
	}
	
	protected final void notifyServerReady() {
		printBanner();
		notifyServerReady0();
	}
	
	protected final void printBanner() {
		logger.info("\n{}\n", new EzyBannerPrinter().getBannerString());
	}
	
	protected void notifyServerReady0() {
		EzyEvent event = new EzySimpleServerReadyEvent();
        context.handleEvent(EzyEventType.SERVER_READY, event);
        context.broadcast(EzyEventType.SERVER_READY, event, true);
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
