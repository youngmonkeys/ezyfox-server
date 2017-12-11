package com.tvd12.ezyfoxserver;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyContexts;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleServerReadyEvent;
import com.tvd12.ezyfoxserver.setting.EzyHttpSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.util.EzyBannerPrinter;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

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
		startLocalBootstrap();
		startHttpBootstrap();
		startOtherBootstraps(this::notifyServerReady);
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
		return EzySimpleServerReadyEvent.builder().build();
	}
	
	protected EzySettings getServerSettings() {
	    return EzyContexts.getSettings(context);
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
