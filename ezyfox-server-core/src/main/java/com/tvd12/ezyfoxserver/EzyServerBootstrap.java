package com.tvd12.ezyfoxserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyDestroyable;
import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyServerReadyEventImpl;

import lombok.Setter;

public abstract class EzyServerBootstrap implements EzyStartable, EzyDestroyable {
	
	@Setter
	protected EzyServerContext context;
	@Setter
	private EzyBootstrap localBootstrap;
	
	@Override
	public void start() throws Exception {
		startLocalBootstrap();
		startOtherBootstraps();
		notifyServerReady();
	}
	
	protected abstract void startOtherBootstraps() throws Exception;
	
	public void destroy() {
		localBootstrap.destroy();
	}
	
	protected void startLocalBootstrap() throws Exception {
		getLogger().debug("starting local bootstrap ....");
		localBootstrap.start();
		getLogger().debug("starting local bootstrap successful");
	}
	
	protected void notifyServerReady() {
		context.get(EzyFireEvent.class).fire(EzyEventType.SERVER_READY, newServerReadyEvent());
	}
	
	protected EzyEvent newServerReadyEvent() {
		return EzyServerReadyEventImpl.builder().server(context.getBoss()).build();
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
	
	
}
