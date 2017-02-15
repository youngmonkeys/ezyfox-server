package com.tvd12.ezyfoxserver.config;

import com.tvd12.properties.file.annotation.Property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EzyFoxConfig {

	@Property("ezyfox.home")
	private String ezyfoxHome;
	
	@Property("ezyfox.version")
	private String ezyfoxVersion;
	
	@Property("logback.config.file")
	private String logbackConfigFile;
	
}
