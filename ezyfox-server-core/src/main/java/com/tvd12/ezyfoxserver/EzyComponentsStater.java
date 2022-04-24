package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyServerContexts;
import com.tvd12.ezyfoxserver.setting.EzySettings;

public abstract class EzyComponentsStater extends EzyLoggable implements EzyStartable {

    protected final EzySettings settings;
    protected final EzyServerContext serverContext;

    protected EzyComponentsStater(Builder<?, ?> builder) {
        this.serverContext = builder.serverContext;
        this.settings = EzyServerContexts.getSettings(serverContext);
    }

    @Override
    public abstract void start();

    @SuppressWarnings("unchecked")
    public static abstract class Builder
        <T extends EzyComponentsStater, B extends Builder<T, B>>
        implements EzyBuilder<T> {

        protected EzyServerContext serverContext;

        public B serverContext(EzyServerContext serverContext) {
            this.serverContext = serverContext;
            return (B) this;
        }

    }

}
