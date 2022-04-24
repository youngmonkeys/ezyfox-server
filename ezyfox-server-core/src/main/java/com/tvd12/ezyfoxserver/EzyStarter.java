/**
 *
 */
package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzyConfigLoader;
import com.tvd12.ezyfoxserver.config.EzySimpleConfigLoader;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySettingsDecorator;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tavandung12
 *
 */
@SuppressWarnings("rawtypes")
public abstract class EzyStarter implements EzyStartable {

    private final String configFile;
    private final EzySettingsDecorator settingsDecorator;
    @Getter
    private EzyServerContext serverContext;

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
        if (config.isPrintSettings()) {
            getLogger().info("settings: \n{}", server.toString());
        }
        EzyServerBootstrap serverBoostrap = newServerBoostrap(server);
        serverBoostrap.start();
        serverContext = serverBoostrap.getContext();
        serverContext.setProperty(EzyServerBootstrap.class, serverBoostrap);
    }

    protected EzyServerBootstrap newServerBoostrap(EzyServer server) {
        return newServerBootstrapBuilder().server(server).build();
    }

    protected abstract EzyServerBootstrapBuilder newServerBootstrapBuilder();

    protected void setSystemProperties(EzyConfig config) {}

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

    protected Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
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
