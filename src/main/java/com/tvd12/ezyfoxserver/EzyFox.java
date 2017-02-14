package com.tvd12.ezyfoxserver;

import java.util.Map;

import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyFoxConfig;
import com.tvd12.ezyfoxserver.config.EzyFoxSettings;
import com.tvd12.ezyfoxserver.service.EzyFoxXmlReading;

import lombok.Setter;
import lombok.ToString;

/**
 * Hello world!
 *
 */
@Setter
@ToString
public class EzyFox {

	private EzyFoxConfig config;
	private EzyFoxXmlReading xmlReading;
	private EzyFoxSettings settings;
	private ClassLoader classLoader;
	private Map<String, EzyAppClassLoader> appClassLoaders;
    
}
