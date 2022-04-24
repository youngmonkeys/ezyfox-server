package com.tvd12.ezyfoxserver.embedded;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.EzyRunner;
import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.setting.EzySettings;

public class EzyEmbeddedRunner extends EzyRunner {

    protected final EzyConfig config;
    protected final EzySettings settings;

    protected EzyEmbeddedRunner(Builder builder) {
        this.config = builder.config;
        this.settings = builder.settings;
    }

    @Override
    protected void validateArguments(String[] args) {}

    @Override
    protected EzyStarter.Builder<?> newStarterBuilder() {
        return EzyEmbeddedStarter.builder()
                .config(config)
                .settings(settings);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements EzyBuilder<EzyEmbeddedRunner> {

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
        public EzyEmbeddedRunner build() {
            return new EzyEmbeddedRunner(this);
        }

    }

}
