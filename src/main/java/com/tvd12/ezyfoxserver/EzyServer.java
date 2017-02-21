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
import com.tvd12.ezyfoxserver.loader.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.loader.EzyPluginEntryLoader;
import com.tvd12.ezyfoxserver.service.EzyJsonMapping;
import com.tvd12.ezyfoxserver.service.EzyXmlReading;

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

	private EzyConfig config;
	private EzySettings settings;
	
	@JsonIgnore
	private ClassLoader classLoader;
	@JsonIgnore
	private EzyXmlReading xmlReading;
	@JsonIgnore
	private EzyJsonMapping jsonMapping;
	@JsonIgnore
	private Map<String, EzyAppClassLoader> appClassLoaders;
	
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
