package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyServerReadyEventImpl;
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
	@Setter
	protected EzyHttpBootstrap httpBootstrap;
	
	@Override
	public void start() throws Exception {
		startLocalBootstrap();
		startHttpBootstrap();
		startOtherBootstraps(this::notifyServerReady);
	}
	
	protected void startHttpBootstrap() throws Exception {
	    getLogger().debug("starting http server bootstrap ....");
	    httpBootstrap.start();
	    getLogger().debug("http server bootstrap has started");
	}
	
	protected abstract void startOtherBootstraps(Runnable callback) throws Exception;
	
	@Override
	public void destroy() {
		localBootstrap.destroy();
		httpBootstrap.destroy();
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
		return EzyServerReadyEventImpl.builder().build();
	}
	
}
