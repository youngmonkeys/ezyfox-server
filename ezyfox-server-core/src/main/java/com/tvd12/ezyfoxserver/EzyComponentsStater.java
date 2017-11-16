package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

public abstract class EzyComponentsStater extends EzyLoggable implements EzyStartable {

    protected EzySettings settings;
    protected EzyServerContext serverContext;
    
    protected EzyComponentsStater(Builder<?,?> builder) {
        this.settings = builder.settings;
        this.serverContext = builder.serverContext;
    }
    
    @Override
    public abstract void start();
    
    @SuppressWarnings("unchecked")
    public static abstract class Builder
            <T extends EzyComponentsStater,B extends Builder<T,B>> 
            implements EzyBuilder<T> {
        protected EzySettings settings;
        protected EzyServerContext serverContext;
        
        public B settings(EzySettings settings) {
            this.settings = settings;
            return (B)this;
        }
        public B serverContext(EzyServerContext serverContext) {
            this.serverContext = serverContext;
            return (B)this;
        }
        
    }
    
}
