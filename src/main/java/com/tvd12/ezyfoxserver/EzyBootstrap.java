package com.tvd12.ezyfoxserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.entity.EzyDestroyable;
import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.loader.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.loader.EzyPluginEntryLoader;

import lombok.Builder;

@Builder
public class EzyBootstrap implements EzyStartable, EzyDestroyable {

	private EzyServer ezyFox;
	
	@Override
	public void start() throws Exception {
		startAllPlugins();
		startAllApps();
	}
	
	@Override
	public void destroy() {
		//TODO destroy apps and plugins
	}
	
	//====================== apps ===================
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
	
	private void startApp(final EzyAppEntryLoader loader) throws Exception {
		startApp(loader.load());
	}
	
	private void startApp(final EzyAppEntry entry) throws Exception {
		entry.start();
	}
	//=================================================
	
	//===================== plugins ===================
	private void startAllPlugins() {
		ezyFox.getPluginNames().forEach(this::startPlugin);
	}
	
	private void startPlugin(final String pluginName) {
		try {
			getLogger().info("plugin " + pluginName + " loading...");
			startPlugin(ezyFox.newPluginEntryLoader(pluginName));
			getLogger().info("plugin" + pluginName + " loaded");
		}
		catch(Exception e) {
			getLogger().error("can not start plugin " + pluginName, e);
		} 
	}
	
	private void startPlugin(final EzyPluginEntryLoader loader) throws Exception {
		startPlugin(loader.load());
	}
	
	private void startPlugin(final EzyPluginEntry entry) throws Exception {
		entry.start();
	}
	//=================================================
	
	private Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}
