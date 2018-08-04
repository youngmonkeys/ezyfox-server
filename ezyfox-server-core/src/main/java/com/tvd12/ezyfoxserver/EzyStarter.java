/**
 * 
 */
package com.tvd12.ezyfoxserver;

import static com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting.SETTINGS;

import java.io.File;
import java.nio.file.Paths;

import org.apache.log4j.PropertyConfigurator;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzyConfigLoader;
import com.tvd12.ezyfoxserver.config.EzySimpleConfigLoader;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

/**
 * @author tavandung12
 *
 */
@SuppressWarnings("rawtypes")
public abstract class EzyStarter extends EzyLoggable implements EzyStartable {

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

    protected void startEzyFox(EzyServer server) throws Exception {
        getLogger().info("settings: \n{}", server.toString());
        EzyServerBootstrap serverBoostrap = newServerBoostrap(server);
        serverBoostrap.start();
    }

    protected EzyServerBootstrap newServerBoostrap(EzyServer server) {
        return newServerBootstrapBuilder().server(server).build();
    }

    protected abstract EzyServerBootstrapBuilder newServerBootstrapBuilder();

    protected void setSystemProperties(EzyConfig config) {
        PropertyConfigurator.configure(getLoggerConfigFile(config));
        System.setProperty("log4j.configuration", getLoggerConfigFile(config));
        System.setProperty("logging.config", getLoggerConfigFile(config));
    }

    protected EzyServer loadEzyFox(EzyConfig config) {
        return newLoader().config(config).classLoader(getClassLoader()).load();
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
        return EzySimpleServer.class.getClassLoader();
    }

    protected EzyConfig readConfig(String configFile) throws Exception {
        return getConfigLoader().load(configFile);
    }

    protected String getLoggerConfigFile(EzyConfig config) {
        return getPath(config.getEzyfoxHome(), SETTINGS, config.getLoggerConfigFile());
    }

    protected String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<B extends Builder<B>> implements EzyBuilder<EzyStarter> {

        protected String configFile;

        public B configFile(String configFile) {
            this.configFile = configFile;
            return (B) this;
        }

    }
}
