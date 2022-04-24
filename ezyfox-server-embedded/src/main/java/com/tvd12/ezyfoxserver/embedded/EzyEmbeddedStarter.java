package com.tvd12.ezyfoxserver.embedded;

import com.tvd12.ezyfoxserver.EzyLoader;
import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.nio.EzyNioStarter;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

public class EzyEmbeddedStarter extends EzyNioStarter {

    protected final EzyConfig config;
    protected final EzySettings settings;

    protected EzyEmbeddedStarter(Builder builder) {
        super(builder);
        this.config = builder.config;
        this.settings = builder.settings;
    }

    @Override
    protected EzyConfig readConfig(String configFile) throws Exception {
        if(config != null)
            return config;
        return super.readConfig(configFile);
    }

    @Override
    protected EzyLoader newLoader() {
        return new EzyEmbeddedLoader() {
            @SuppressWarnings("rawtypes")
            @Override
            protected EzySimpleSessionManager.Builder 
                    createSessionManagerBuilder(EzySettings settings) {
                return newSessionManagerBuilder(settings);
            }
            
            @Override
            protected EzySettings readSettings() {
                return settings;
            }
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzyNioStarter.Builder {

        protected EzyConfig config;
        protected EzySettings settings;

        public Builder config(EzyConfig config) {
            this.config = config;
            return this;
        }

        public Builder settings(EzySettings settings) {
            this.settings = settings;
            return this;
        }

        @Override
        public EzyStarter build() {
            return new EzyEmbeddedStarter(this);
        }

    }
}
