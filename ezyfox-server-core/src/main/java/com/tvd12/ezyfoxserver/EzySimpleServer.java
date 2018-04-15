package com.tvd12.ezyfoxserver;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyResponseApiAware;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.mapping.jackson.EzyJsonMapper;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManagerAware;

import lombok.Getter;
import lombok.Setter;

/**
 * Hello world!
 *
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("rawtypes")
public class EzySimpleServer 
        extends EzyComponent 
        implements EzyServer, EzyResponseApiAware, EzySessionManagerAware, EzyDestroyable {

	protected EzyConfig config;
	protected EzySettings settings;
	
	@JsonIgnore
	protected ClassLoader classLoader;
	@JsonIgnore
	protected EzyJsonMapper jsonMapper;
	@JsonIgnore
	protected EzyStatistics statistics;
	@JsonIgnore
	protected EzyServerControllers controllers;
	@JsonIgnore
    protected EzyResponseApi responseApi;
    @JsonIgnore
	protected EzySessionManager sessionManager;
	@JsonIgnore
    protected Map<String, EzyAppClassLoader> appClassLoaders;
	
	@Override
	public String getVersion() {
	    return "1.0.0";
	}
	
	@Override
	public void destroy() {
	    ((EzyDestroyable)sessionManager).destroy();
	}
	
	@Override
	public String toString() {
		return jsonMapper.writeAsString(this);
	}
    
}
