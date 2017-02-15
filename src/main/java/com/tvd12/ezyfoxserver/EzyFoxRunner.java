/**
 * 
 */
package com.tvd12.ezyfoxserver;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.config.EzyFoxConfig;
import com.tvd12.ezyfoxserver.loader.EzyFoxConfigLoader;
import com.tvd12.ezyfoxserver.loader.impl.EzyFoxConfigLoaderImpl;
import com.tvd12.ezyfoxserver.service.EzyFoxJsonMapping;
import com.tvd12.ezyfoxserver.service.EzyFoxXmlReading;
import com.tvd12.ezyfoxserver.service.impl.EzyFoxJsonMappingImpl;
import com.tvd12.ezyfoxserver.service.impl.EzyFoxXmlReadingImpl;

/**
 * @author tavandung12
 *
 */
public class EzyFoxRunner {
    
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
    
    private static void startSystem(final EzyFoxConfig config) {
    	setSystemProperties(config);
    	startEzyFox(config);
    }
    
    private static void startEzyFox(final EzyFoxConfig config) {
    	startEzyFox(loadEzyFox(config));
    }
    
    private static void startEzyFox(final EzyFox ezyFox) {
    	getLogger().info(ezyFox.toString());
    	getBoostrap(ezyFox).boost();
    }
    
    private static EzyFoxBootstrap getBoostrap(final EzyFox ezyFox) {
    	return EzyFoxBootstrap.builder().ezyFox(ezyFox).build();
    }
    
    private static void setSystemProperties(final EzyFoxConfig config) {
    	System.setProperty("logback.configurationFile", getLogbackConfigFile(config));
    }
    
    private static EzyFox loadEzyFox(final EzyFoxConfig config) {
    	return EzyFoxLoader.newInstance()
    			.config(config)
    			.xmlReading(getXmlReading())
    			.jsonMapping(getJsonMapping())
    			.classLoader(getClassLoader())
    			.load();
    }
    
    private static EzyFoxConfig readConfig(final String configFile) throws Exception {
    	return getConfigLoader(configFile).load();
    }
    
    private static EzyFoxConfigLoader getConfigLoader(final String configFile) {
    	return EzyFoxConfigLoaderImpl.newInstance().filePath(configFile);
    }

    private static ClassLoader getClassLoader() {
        return EzyFox.class.getClassLoader();
    }
    
    private static String getLogbackConfigFile(final EzyFoxConfig config) {
    	return getPath(config.getEzyfoxHome(), "settings", config.getLogbackConfigFile()); 
    }
    
    private static String getPath(final String first, final String... more) {
        return Paths.get(first, more).toString();
    }
    
    private static EzyFoxXmlReading getXmlReading() {
    	return EzyFoxXmlReadingImpl.builder()
    			.classLoader(getClassLoader())
    			.contextPath("com.tvd12.ezyfoxserver")
    			.build();
    }
    
    private static EzyFoxJsonMapping getJsonMapping() {
    	return EzyFoxJsonMappingImpl.builder().build();
    }
    
    private static Logger getLogger() {
    	return LoggerFactory.getLogger(EzyFoxRunner.class);
    }
}
