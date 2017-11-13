package com.tvd12.ezyfoxserver;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.mapping.jackson.EzyJsonMapper;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyRequestMappers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;

import lombok.Getter;
import lombok.Setter;

/**
 * Hello world!
 *
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EzySimpleServer implements EzyServer {

	protected EzyConfig config;
	protected EzySettings settings;
	
	@JsonIgnore
	protected EzyManagers managers;
	@JsonIgnore
	protected ClassLoader classLoader;
	@JsonIgnore
	protected EzyJsonMapper jsonMapper;
	@JsonIgnore
	protected EzyStatistics statistics;
	@JsonIgnore
	protected EzyServerControllers controllers;
	@JsonIgnore
	protected EzyRequestMappers requestMappers;
	@JsonIgnore
    protected EzyEventPluginsMapper eventPluginsMapper;
	@JsonIgnore
	protected Map<String, EzyAppClassLoader> appClassLoaders;
	
	@Override
	public String getVersion() {
	    return "1.0.0";
	}
	
	@JsonIgnore
	@Override
	public Set<Integer> getAppIds() {
		return settings.getAppIds();
	}
	
	@Override
	public EzyAppSetting getAppById(Integer id) {
		return settings.getAppById(id);
	}
	
	//==================== plugins ================//
	@JsonIgnore
	@Override
	public Set<Integer> getPluginIds() {
		return settings.getPluginIds();
	}
	
	@Override
	public EzyPluginSetting getPluginById(Integer id) {
		return settings.getPluginById(id);
	}
	//=============================================//
	
	@Override
	public String toString() {
		return jsonMapper.writeAsString(this);
	}
    
}
