/**
 * 
 */
package com.tvd12.ezyfoxserver;

import java.io.File;
import java.nio.file.Paths;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.loader.EzyConfigLoader;
import com.tvd12.ezyfoxserver.loader.impl.EzyConfigLoaderImpl;
import com.tvd12.ezyfoxserver.service.EzyJsonMapping;
import com.tvd12.ezyfoxserver.service.EzyXmlReading;
import com.tvd12.ezyfoxserver.service.impl.EzyJsonMappingImpl;
import com.tvd12.ezyfoxserver.service.impl.EzyXmlReadingImpl;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyControllersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyManagersImpl;

/**
 * @author tavandung12
 *
 */
public abstract class EzyStarter 
		extends EzyLoggable 
		implements EzyStartable {
	
	private String configFile;
	
	protected EzyStarter(Builder<?> builder) {
		this.configFile = builder.configFile;
	}
	
	@Override
	public void start() throws Exception {
		startSystem();
	}
	
	protected void startSystem() throws Exception {
    	startSystem(readConfig(new File(configFile).toString()));
    }
    
    protected void startSystem(EzyConfig config) throws Exception {
    	setSystemProperties(config);
    	startEzyFox(config);
    }
    
    protected void startEzyFox(EzyConfig config) throws Exception {
    	startEzyFox(loadEzyFox(config));
    }
    
    protected void startEzyFox(EzyServer ezyFox) throws Exception {
    	getLogger().info(ezyFox.toString());
    	newServerBoostrap(ezyFox).start();
    }
    
    protected EzyServerBootstrap newServerBoostrap(EzyServer ezyFox) {
    	return newServerBootstrapBuilder()
    			.port(3005)
    			.wsport(2208)
    			.boss(ezyFox)
    			.build();
    }
    
    protected abstract EzyServerBootstrapBuilder newServerBootstrapBuilder();
    
    protected void setSystemProperties(EzyConfig config) {
    	System.setProperty("logback.configurationFile", getLogbackConfigFile(config));
    }
    
    protected EzyServer loadEzyFox(EzyConfig config) {
    	return EzyLoader.newInstance()
    			.config(config)
    			.managers(newManagers())
    			.controllers(newControllers())
    			.xmlReading(getXmlReading())
    			.jsonMapping(getJsonMapping())
    			.classLoader(getClassLoader())
    			.load();
    }
    
    protected EzyConfig readConfig(String configFile) throws Exception {
    	return getConfigLoader(configFile).load();
    }
    
    protected EzyConfigLoader getConfigLoader(String configFile) {
    	return EzyConfigLoaderImpl.newInstance().filePath(configFile);
    }
    
    public EzyManagers newManagers() {
    	return EzyManagersImpl.builder().build();
    }
    
    public EzyControllers newControllers() {
    	return EzyControllersImpl.builder().build();
    }

    protected ClassLoader getClassLoader() {
        return EzyServer.class.getClassLoader();
    }
    
    protected String getLogbackConfigFile(EzyConfig config) {
    	return getPath(config.getEzyfoxHome(), "settings", config.getLogbackConfigFile()); 
    }
    
    protected String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }
    
    protected EzyXmlReading getXmlReading() {
    	return EzyXmlReadingImpl.builder()
    			.classLoader(getClassLoader())
    			.contextPath("com.tvd12.ezyfoxserver")
    			.build();
    }
    
    protected EzyJsonMapping getJsonMapping() {
    	return EzyJsonMappingImpl.builder().build();
    }
    
    public abstract static class Builder<B extends Builder<B>> 
    		implements EzyBuilder<EzyStarter> {
    	protected String configFile;
    	
    	public B configFile(String configFile) {
    		this.configFile = configFile;
    		return getThis();
    	}
    	
    	@SuppressWarnings("unchecked")
		protected B getThis() {
    		return (B)this;
    	}
    	
    }
}
