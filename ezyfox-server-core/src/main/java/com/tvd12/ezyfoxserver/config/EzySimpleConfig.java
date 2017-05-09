package com.tvd12.ezyfoxserver.config;

import com.tvd12.ezyfoxserver.util.EzyReturner;
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
	
	@Property("ezyfox.version")
	private String ezyfoxVersion;
	
	@Property("logger.config.file")
	private String loggerConfigFile;
	
	public String getEzyfoxHome() {
	    return EzyReturner.returnNotNull(ezyfoxHome, "");
	}
	
}
