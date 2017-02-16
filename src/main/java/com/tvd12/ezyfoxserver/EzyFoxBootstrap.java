package com.tvd12.ezyfoxserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.ext.EzyFoxAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyFoxPluginEntry;
import com.tvd12.ezyfoxserver.loader.EzyFoxAppEntryLoader;
import com.tvd12.ezyfoxserver.loader.EzyFoxPluginEntryLoader;

import lombok.Builder;

@Builder
public class EzyFoxBootstrap {

	private EzyFox ezyFox;
	
	public void boost() {
		startAllApps();
		startAllPlugins();
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
	
	private void startApp(final EzyFoxAppEntryLoader loader) throws Exception {
		startApp(loader.load());
	}
	
	private void startApp(final EzyFoxAppEntry entry) throws Exception {
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
	
	private void startPlugin(final EzyFoxPluginEntryLoader loader) throws Exception {
		startPlugin(loader.load());
	}
	
	private void startPlugin(final EzyFoxPluginEntry entry) throws Exception {
		entry.start();
	}
	//=================================================
	
	private Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}
