package com.tvd12.ezyfoxserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyDestroyable;
import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.loader.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.loader.EzyPluginEntryLoader;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;

import lombok.Builder;

@Builder
public class EzyBootstrap implements EzyStartable, EzyDestroyable {

	protected EzyServerContext context;
	
	@Override
	public void start() throws Exception {
		startAllPlugins();
		startAllApps();
		startManagers();
	}
	
	@Override
	public void destroy() {
		//TODO destroy apps and plugins
	}
	
	protected void startManagers() {
		getManagers().startManagers();
	}
	
	//====================== apps ===================
	protected void startAllApps() {
		getServer().getAppNames().forEach(this::startApp);
	}
	
	protected void startApp(String appName) {
		try {
			getLogger().debug("app " + appName + " loading...");
			startApp(appName, getServer().newAppEntryLoader(appName));
			getLogger().debug("app " + appName + " loaded");
		}
		catch(Exception e) {
			getLogger().error("can not start app " + appName, e);
		} 
	}
	
	protected void startApp(String appName, EzyAppEntryLoader loader) throws Exception {
		startApp(appName, loader.load());
	}
	
	protected void startApp(String appName, EzyAppEntry entry) throws Exception {
		entry.config(getAppContext(appName));
		entry.start();
	}
	//=================================================
	
	//===================== plugins ===================
	protected void startAllPlugins() {
		getServer().getPluginNames().forEach(this::startPlugin);
	}
	
	protected void startPlugin(String pluginName) {
		try {
			getLogger().debug("plugin " + pluginName + " loading...");
			startPlugin(pluginName, getServer().newPluginEntryLoader(pluginName));
			getLogger().debug("plugin " + pluginName + " loaded");
		}
		catch(Exception e) {
			getLogger().error("can not start plugin " + pluginName, e);
		} 
	}
	
	protected void startPlugin(String pluginName, EzyPluginEntryLoader loader) 
			throws Exception {
		startPlugin(pluginName, loader.load());
	}
	
	protected void startPlugin(String pluginName, EzyPluginEntry entry) throws Exception {
		entry.config(getPluginContext(pluginName));
		entry.start();
	}
	//=================================================
	
	protected EzyServer getServer() {
		return context.getBoss();
	}
	
	protected EzyAppContext getAppContext(String appName) {
		return context.getAppContext(appName);
	}
	
	protected EzyPluginContext getPluginContext(String pluginName) {
		return context.getPluginContext(pluginName);
	}
	
	protected EzyManagers getManagers() {
		return getServer().getManagers();
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}
