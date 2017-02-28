/**
 * 
 */
package com.tvd12.ezyfoxserver;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.builder.impl.EzyServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.entity.EzyDestroyable;
import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.loader.EzyConfigLoader;
import com.tvd12.ezyfoxserver.loader.impl.EzyConfigLoaderImpl;
import com.tvd12.ezyfoxserver.reflect.EzyClassUtil;
import com.tvd12.ezyfoxserver.service.EzyJsonMapping;
import com.tvd12.ezyfoxserver.service.EzyXmlReading;
import com.tvd12.ezyfoxserver.service.impl.EzyJsonMappingImpl;
import com.tvd12.ezyfoxserver.service.impl.EzyXmlReadingImpl;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author tavandung12
 *
 */
public class EzyStarter implements EzyStartable, EzyDestroyable {
	
	private String configFile;
	
	protected EzyStarter(Builder builder) {
		this.configFile = builder.configFile;
	}
	
	@Override
	public void start() throws Exception {
		startSystem();
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
    
	protected void startSystem() throws Exception {
    	startSystem(readConfig(new File(configFile).toString()));
    }
    
    protected void startSystem(final EzyConfig config) throws Exception {
    	setSystemProperties(config);
    	startEzyFox(config);
    }
    
    protected void startEzyFox(final EzyConfig config) throws Exception {
    	startEzyFox(loadEzyFox(config));
    }
    
    protected void startEzyFox(final EzyServer ezyFox) throws Exception {
    	getLogger().info(ezyFox.toString());
    	newServerBoostrap(newLocalBoostrap(ezyFox)).start();
    }
    
    protected EzyServerBootstrap newServerBoostrap(final EzyBootstrap localBootstrap) {
    	return newServerBootstrapBuilder()
    			.port(3005)
    			.localBootstrap(localBootstrap)
    			.parentGroup(newParentEventLoopGroup())
    			.codecCreator(newCodecCreator())
    			.build();
    }
    
    protected EventLoopGroup newParentEventLoopGroup() {
    	return new NioEventLoopGroup(0, EzyExecutors.newThreadFactory("parenteventloopgroup"));
    }
    
    protected EzyBootstrap newLocalBoostrap(final EzyServer ezyFox) {
    	return EzyBootstrap.builder().ezyFox(ezyFox).build();
    }
    
    protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
    	return new EzyServerBootstrapBuilderImpl();
    }
    
    protected void setSystemProperties(final EzyConfig config) {
    	System.setProperty("logback.configurationFile", getLogbackConfigFile(config));
    }
    
    protected EzyServer loadEzyFox(final EzyConfig config) {
    	return EzyLoader.newInstance()
    			.config(config)
    			.xmlReading(getXmlReading())
    			.jsonMapping(getJsonMapping())
    			.classLoader(getClassLoader())
    			.load();
    }
    
    protected EzyConfig readConfig(final String configFile) throws Exception {
    	return getConfigLoader(configFile).load();
    }
    
    protected EzyConfigLoader getConfigLoader(final String configFile) {
    	return EzyConfigLoaderImpl.newInstance().filePath(configFile);
    }

    protected ClassLoader getClassLoader() {
        return EzyServer.class.getClassLoader();
    }
    
    protected String getLogbackConfigFile(final EzyConfig config) {
    	return getPath(config.getEzyfoxHome(), "settings", config.getLogbackConfigFile()); 
    }
    
    protected String getPath(final String first, final String... more) {
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
    
    protected EzyCodecCreator newCodecCreator() {
    	return EzyClassUtil.newInstance(getCodecCreatorClassName());
    }
    
    protected String getCodecCreatorClassName() {
    	return "com.tvd12.ezyfoxserver.codec.MsgPackCodecCreator";
    }
    
    protected Logger getLogger() {
    	return LoggerFactory.getLogger(EzyStarter.class);
    }
    
    public static class Builder implements EzyBuilder<EzyStarter> {
    	protected String configFile;
    	
    	public Builder configFile(String configFile) {
    		this.configFile = configFile;
    		return this;
    	}
    	
    	public EzyStarter build() {
    		return new EzyStarter(this);
    	}
    	
    }
}
