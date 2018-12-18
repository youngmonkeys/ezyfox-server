package com.tvd12.ezyfoxserver.config;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.properties.file.annotation.Property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EzySimpleConfig implements EzyConfig {

	@Property("ezyfox.home")
	private String ezyfoxHome;
	
	@Property("logger.config.file")
	private String loggerConfigFile;
	
	public String getEzyfoxHome() {
	    if(ezyfoxHome == null)
	        return "";
	    return ezyfoxHome;
	}
	
	@Override
	public Map<Object, Object> toMap() {
	    Map<Object, Object> map = new HashMap<>();
	    map.put("ezyfoxHome", getEzyfoxHome());
	    map.put("loggerConfigFile", loggerConfigFile);
	    return map;
	}
	
}
