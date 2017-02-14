/**
 * 
 */
package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.config.EzyFoxConfig;
import com.tvd12.ezyfoxserver.loader.EzyFoxConfigLoader;
import com.tvd12.ezyfoxserver.loader.impl.EzyFoxConfigLoaderImpl;
import com.tvd12.ezyfoxserver.service.EzyFoxXmlReading;
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
    
    protected static void validateArguments(final String[] args) {
    	if(args.length == 0)
    		throw new IllegalStateException("must specific config.properties file");
    }
    
    protected static void startSystem(final String configFile) throws Exception {
    	startSystem(readConfig(configFile));
    }
    
    protected static void startSystem(final EzyFoxConfig config) throws Exception {
    	EzyFox ezyFox = loadEzyFox(config);
    	System.out.println(ezyFox);
    }
    
    protected static EzyFox loadEzyFox(final EzyFoxConfig config) {
    	return EzyFoxLoader.newInstance()
    			.config(config)
    			.xmlReading(getXmlReading())
    			.classLoader(getClassLoader())
    			.load();
    }
    
    protected static EzyFoxConfig readConfig(final String configFile) throws Exception {
    	return new EzyFoxConfigLoaderImpl().filePath(configFile).load();
    }
    
    protected static EzyFoxConfigLoader getConfigLoader() {
    	return new EzyFoxConfigLoaderImpl();
    }

    private static ClassLoader getClassLoader() {
        return EzyFox.class.getClassLoader();
    }
    
    private static EzyFoxXmlReading getXmlReading() {
    	return EzyFoxXmlReadingImpl.builder()
    			.classLoader(getClassLoader())
    			.contextPath("com.tvd12.ezyfoxserver")
    			.build();
    }
}
