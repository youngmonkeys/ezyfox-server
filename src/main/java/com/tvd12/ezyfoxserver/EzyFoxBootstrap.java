package com.tvd12.ezyfoxserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.ext.EzyFoxAppEntry;
import com.tvd12.ezyfoxserver.loader.EzyFoxAppEntryLoader;

import lombok.Builder;

@Builder
public class EzyFoxBootstrap {

	private EzyFox ezyFox;
	
	public void boost() {
		startAllApps();
	}
	
	private void startAllApps() {
		ezyFox.getAppNames().forEach(this::startApp);
	}
	
	private void startApp(final String appName) {
		try {
			getLogger().info("app " + appName + " loading...");
			startApp(ezyFox.newAppEntryLoader(appName));
			getLogger().info("app " + appName + " loaded");
		}
		catch(Exception e) {
			getLogger().error("can not start app " + appName, e);
		} 
	}
	
	private void startApp(final EzyFoxAppEntryLoader loader) throws Exception {
		startApp(loader.load());
	}
	
	private void startApp(final EzyFoxAppEntry entry) throws Exception {
		entry.boost();
	}
	
	private Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}
