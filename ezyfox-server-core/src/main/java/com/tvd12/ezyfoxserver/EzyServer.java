package com.tvd12.ezyfoxserver;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyApp;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzyPlugin;
import com.tvd12.ezyfoxserver.config.EzySettings;
import com.tvd12.ezyfoxserver.ext.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntryLoader;
import com.tvd12.ezyfoxserver.service.EzyJsonMapping;
import com.tvd12.ezyfoxserver.service.EzyXmlReading;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;

import lombok.Getter;
import lombok.Setter;

/**
 * Hello world!
 *
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EzyServer {

	protected EzyConfig config;
	protected EzySettings settings;
	
	@JsonIgnore
	protected EzyManagers managers;
	@JsonIgnore
	protected ClassLoader classLoader;
	@JsonIgnore
	protected EzyXmlReading xmlReading;
	@JsonIgnore
	protected EzyJsonMapping jsonMapping;
	@JsonIgnore
	protected EzyControllers controllers;
	@JsonIgnore
	protected Map<String, EzyAppClassLoader> appClassLoaders;
	
	@JsonIgnore
	public Set<String> getAppNames() {
		return settings.getAppNames();
	}
	
	@JsonIgnore
	public Set<Integer> getAppIds() {
		return settings.getAppIds();
	}
	
	public EzyApp getAppByName(final String name) {
		return settings.getAppByName(name);
	}
	
	public EzyApp getAppById(final Integer id) {
		return settings.getAppById(id);
	}
	
	public Class<EzyAppEntryLoader> 
			getAppEntryLoaderClass(final String appName) throws Exception {
		return getAppEntryLoaderClass(getAppByName(appName));
	}
	
	@SuppressWarnings("unchecked")
	public Class<EzyAppEntryLoader> 
			getAppEntryLoaderClass(final EzyApp app) throws Exception {
		return  (Class<EzyAppEntryLoader>) 
			Class.forName(app.getEntryLoader(), true, getClassLoader(app.getName()));
	}
	
	public EzyAppEntryLoader newAppEntryLoader(final String appName) throws Exception {
		return getAppEntryLoaderClass(appName).newInstance();
	}
	
	//==================== plugins ================//
	@JsonIgnore
	public Set<String> getPluginNames() {
		return settings.getPluginNames();
	}
	
	@JsonIgnore
	public Set<Integer> getPluginIds() {
		return settings.getPluginIds();
	}
	
	public EzyPlugin getPluginByName(final String name) {
		return settings.getPluginByName(name);
	}
	
	public EzyPlugin getPluginById(final Integer id) {
		return settings.getPluginById(id);
	}
	
	public Class<EzyPluginEntryLoader> 
			getPluginEntryLoaderClass(final String pluginName) throws Exception {
		return getPluginEntryLoaderClass(getPluginByName(pluginName));
	}
	
	@SuppressWarnings("unchecked")
	public Class<EzyPluginEntryLoader> 
			getPluginEntryLoaderClass(final EzyPlugin plugin) throws Exception {
		return  (Class<EzyPluginEntryLoader>) Class.forName(plugin.getEntryLoader());
	}
	
	public EzyPluginEntryLoader newPluginEntryLoader(final String pluginName) throws Exception {
		return getPluginEntryLoaderClass(pluginName).newInstance();
	}
	//=============================================//
	
	public EzyAppClassLoader getClassLoader(final String appName) {
		if(appClassLoaders.containsKey(appName)) 
			return appClassLoaders.get(appName);
		throw new IllegalArgumentException("has class loader for app " + appName);
	}
	
	@Override
	public String toString() {
		return jsonMapping.writeAsString(this);
	}
    
}
