package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContexts;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

public abstract class EzyZoneComponentsStater extends EzyLoggable implements EzyStartable {

    protected EzyZoneSetting zoneSetting;
    protected EzyZoneContext zoneContext;
    
    protected EzyZoneComponentsStater(Builder<?,?> builder) {
        this.zoneContext = builder.zoneContext;
        this.zoneSetting = EzyZoneContexts.getZoneSetting(zoneContext);
    }
    
    @Override
    public abstract void start();
    
    @SuppressWarnings("unchecked")
    public static abstract class Builder
            <T extends EzyZoneComponentsStater,B extends Builder<T,B>> 
            implements EzyBuilder<T> {
        protected EzyZoneContext zoneContext;
        
        public B zoneContext(EzyZoneContext zoneContext) {
            this.zoneContext = zoneContext;
            return (B)this;
        }
        
    }
    
}
