package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyNioServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

public class EzyNioStarter extends EzyStarter {

    protected EzyNioStarter(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
        return new EzyNioServerBootstrapBuilderImpl();
    }

    @SuppressWarnings({"rawtypes"})
    @Override
    protected EzySimpleSessionManager.Builder newSessionManagerBuilder(EzySettings settings) {
        return EzyNioSessionManagerImpl.builder()
            .maxRequestPerSecond(settings.getSessionManagement().getSessionMaxRequestPerSecond());
    }

    public static class Builder extends EzyStarter.Builder<Builder> {
        @Override
        public EzyStarter build() {
            return new EzyNioStarter(this);
        }
    }
}
