package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContexts;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;

public abstract class EzyZoneComponentsStater extends EzyLoggable implements EzyStartable {

    protected final EzyZoneSetting zoneSetting;
    protected final EzyZoneContext zoneContext;

    protected EzyZoneComponentsStater(Builder<?, ?> builder) {
        this.zoneContext = builder.zoneContext;
        this.zoneSetting = EzyZoneContexts.getZoneSetting(zoneContext);
    }

    @Override
    public abstract void start();

    @SuppressWarnings("unchecked")
    public abstract static class Builder
        <T extends EzyZoneComponentsStater, B extends Builder<T, B>>
        implements EzyBuilder<T> {
        protected EzyZoneContext zoneContext;

        public B zoneContext(EzyZoneContext zoneContext) {
            this.zoneContext = zoneContext;
            return (B) this;
        }
    }
}
