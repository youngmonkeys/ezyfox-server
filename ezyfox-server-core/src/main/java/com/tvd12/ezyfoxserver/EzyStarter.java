/**
 * 
 */
package com.tvd12.ezyfoxserver;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzyConfigLoader;
import com.tvd12.ezyfoxserver.config.EzySimpleConfigLoader;
import com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyProcessor;
import com.tvd12.ezyfoxserver.util.EzyStartable;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

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
        customLoggerConfig(config);
    	setSystemProperties(config);
    	startEzyFox(config);
    }
    
    protected void startEzyFox(EzyConfig config) throws Exception {
    	startEzyFox(loadEzyFox(config));
    }
    
    protected void startEzyFox(EzyServer ezyFox) throws Exception {
    	getLogger().info("settings: \n{}", ezyFox.toString());
    	newServerBoostrap(ezyFox).start();
    }
    
    protected EzyServerBootstrap newServerBoostrap(EzyServer ezyFox) {
    	return newServerBootstrapBuilder()
    			.server(ezyFox)
    			.build();
    }
    
    protected abstract EzyServerBootstrapBuilder newServerBootstrapBuilder();
    
    protected void customLoggerConfig(EzyConfig config) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator cfg = new JoranConfigurator();
        cfg.setContext(loggerContext);
        String configFile = getLoggerConfigFile(config);
        EzyProcessor.processWithLogException(() -> cfg.doConfigure(configFile));
    }
    
    protected void setSystemProperties(EzyConfig config) {
    	System.setProperty("logback.configurationFile", getLoggerConfigFile(config));
    	System.setProperty("logging.config", getLoggerConfigFile(config));
    }
    
    protected EzyServer loadEzyFox(EzyConfig config) {
    	return newLoader()
    			.config(config)
    			.classLoader(getClassLoader())
    			.load();
    }
    
    protected EzyLoader newLoader() {
        return new EzyLoader();
    }
    
    protected EzyConfig readConfig(String configFile) throws Exception {
    	return getConfigLoader().load(configFile);
    }
    
    protected EzyConfigLoader getConfigLoader() {
    	return new EzySimpleConfigLoader();
    }
    
    protected ClassLoader getClassLoader() {
        return EzySimpleServer.class.getClassLoader();
    }
    
    protected String getLoggerConfigFile(EzyConfig config) {
    	return getPath(config.getEzyfoxHome(), EzyFolderNamesSetting.SETTINGS, config.getLoggerConfigFile()); 
    }
    
    protected String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
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
