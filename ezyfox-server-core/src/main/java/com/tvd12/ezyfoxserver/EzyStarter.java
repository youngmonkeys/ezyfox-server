/**
 * 
 */
package com.tvd12.ezyfoxserver;

import static com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting.SETTINGS;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzyConfigLoader;
import com.tvd12.ezyfoxserver.config.EzySimpleConfigLoader;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySettingsDecorator;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

import ch.qos.logback.classic.util.ContextInitializer;
import lombok.Getter;

/**
 * @author tavandung12
 *
 */
@SuppressWarnings("rawtypes")
public abstract class EzyStarter extends EzyLoggable implements EzyStartable {

    @Getter
    private EzyServerContext serverContext;
    private final String configFile;
    private final EzySettingsDecorator settingsDecorator;

    protected EzyStarter(Builder<?> builder) {
        this.configFile = builder.configFile;
        this.settingsDecorator = builder.settingsDecorator;
    }

    @Override
    public void start() throws Exception {
        startSystem();
    }

    protected void startSystem() throws Exception {
        EzyConfig config = readConfig(configFile);
        startSystem(config);
    }

    protected void startSystem(EzyConfig config) throws Exception {
        setSystemProperties(config);
        startEzyFox(config);
    }

    protected void startEzyFox(EzyConfig config) throws Exception {
        startEzyFox(loadEzyFox(config));
    }

    protected void startEzyFox(EzyServer server) throws Exception {
        EzyConfig config = server.getConfig();
        if(config.isPrintSettings())
            logger.info("settings: \n{}", server.toString());
        EzyServerBootstrap serverBoostrap = newServerBoostrap(server);
        serverBoostrap.start();
        serverContext = serverBoostrap.getContext();
        serverContext.setProperty(EzyServerBootstrap.class, serverBoostrap);
    }

    protected EzyServerBootstrap newServerBoostrap(EzyServer server) {
        return newServerBootstrapBuilder().server(server).build();
    }

    protected abstract EzyServerBootstrapBuilder newServerBootstrapBuilder();

    protected void setSystemProperties(EzyConfig config) {
    	String loggerConfigFile = getLoggerConfigFile(config);
    	if(!Files.exists(Paths.get(loggerConfigFile)))
    		return;
        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, loggerConfigFile);
    }

    protected EzyServer loadEzyFox(EzyConfig config) {
        return newLoader()
                .config(config)
                .classLoader(getClassLoader())
                .settingsDecorator(settingsDecorator)
                .load();
    }

    protected EzyLoader newLoader() {
        return new EzyLoader() {
            @Override
            protected EzySimpleSessionManager.Builder 
                    createSessionManagerBuilder(EzySettings settings) {
                return newSessionManagerBuilder(settings);
            }
        };
    }

    protected abstract EzySimpleSessionManager.Builder 
            newSessionManagerBuilder(EzySettings settings);

    protected EzyConfigLoader getConfigLoader() {
        return new EzySimpleConfigLoader();
    }

    protected ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    protected EzyConfig readConfig(String configFile) throws Exception {
        return getConfigLoader().load(configFile);
    }

    protected String getLoggerConfigFile(EzyConfig config) {
    	String loggerFile = config.getLoggerConfigFile();
    	if(EzyStrings.isNoContent(loggerFile))
    		loggerFile = "log4j.properties";
        return getPath(config.getEzyfoxHome(), SETTINGS, loggerFile);
    }

    protected String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<B extends Builder<B>> implements EzyBuilder<EzyStarter> {

        protected String configFile;
        protected EzySettingsDecorator settingsDecorator;

        public B configFile(String configFile) {
            this.configFile = configFile;
            return (B) this;
        }
        
        public B settingsDecorator(EzySettingsDecorator settingsDecorator) {
            this.settingsDecorator = settingsDecorator;
            return (B) this;
        }
    }
}
