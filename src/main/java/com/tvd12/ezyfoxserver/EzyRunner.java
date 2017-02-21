/**
 * 
 */
package com.tvd12.ezyfoxserver;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.loader.EzyConfigLoader;
import com.tvd12.ezyfoxserver.loader.impl.EzyConfigLoaderImpl;
import com.tvd12.ezyfoxserver.service.EzyJsonMapping;
import com.tvd12.ezyfoxserver.service.EzyXmlReading;
import com.tvd12.ezyfoxserver.service.impl.EzyJsonMappingImpl;
import com.tvd12.ezyfoxserver.service.impl.EzyXmlReadingImpl;

/**
 * @author tavandung12
 *
 */
public class EzyRunner {
    
    public static void main(final String[] args) throws Exception {
    	validateArguments(args);
    	startSystem(args[0]);
    }
    
    private static void validateArguments(final String[] args) {
    	if(args.length == 0)
    		throw new IllegalStateException("must specific config.properties file");
    }
    
    private static void startSystem(final String configFile) throws Exception {
    	startSystem(readConfig(new File(configFile).toString()));
    }
    
    private static void startSystem(final EzyConfig config) {
    	setSystemProperties(config);
    	startEzyFox(config);
    }
    
    private static void startEzyFox(final EzyConfig config) {
    	startEzyFox(loadEzyFox(config));
    }
    
    private static void startEzyFox(final EzyServer ezyFox) {
    	getLogger().info(ezyFox.toString());
    	getBoostrap(ezyFox).boost();
    }
    
    private static EzyBootstrap getBoostrap(final EzyServer ezyFox) {
    	return EzyBootstrap.builder().ezyFox(ezyFox).build();
    }
    
    private static void setSystemProperties(final EzyConfig config) {
    	System.setProperty("logback.configurationFile", getLogbackConfigFile(config));
    }
    
    private static EzyServer loadEzyFox(final EzyConfig config) {
    	return EzyLoader.newInstance()
    			.config(config)
    			.xmlReading(getXmlReading())
    			.jsonMapping(getJsonMapping())
    			.classLoader(getClassLoader())
    			.load();
    }
    
    private static EzyConfig readConfig(final String configFile) throws Exception {
    	return getConfigLoader(configFile).load();
    }
    
    private static EzyConfigLoader getConfigLoader(final String configFile) {
    	return EzyConfigLoaderImpl.newInstance().filePath(configFile);
    }

    private static ClassLoader getClassLoader() {
        return EzyServer.class.getClassLoader();
    }
    
    private static String getLogbackConfigFile(final EzyConfig config) {
    	return getPath(config.getEzyfoxHome(), "settings", config.getLogbackConfigFile()); 
    }
    
    private static String getPath(final String first, final String... more) {
        return Paths.get(first, more).toString();
    }
    
    private static EzyXmlReading getXmlReading() {
    	return EzyXmlReadingImpl.builder()
    			.classLoader(getClassLoader())
    			.contextPath("com.tvd12.ezyfoxserver")
    			.build();
    }
    
    private static EzyJsonMapping getJsonMapping() {
    	return EzyJsonMappingImpl.builder().build();
    }
    
    private static Logger getLogger() {
    	return LoggerFactory.getLogger(EzyRunner.class);
    }
}
