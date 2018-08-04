package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Set;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyHashMapSet;
import com.tvd12.ezyfox.util.EzyMapSet;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginsSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;

public class EzyEventPluginsMapperImpl implements EzyEventPluginsMapper {

    protected final EzyMapSet<EzyConstant, EzyPluginSetting> eventsPluginss;
    
    protected EzyEventPluginsMapperImpl(Builder builder) {
        this.eventsPluginss = builder.newEventsPluginss();
    }
    
    @Override
    public Set<EzyPluginSetting> getPlugins(EzyConstant eventType) {
        return eventsPluginss.getItems(eventType);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyEventPluginsMapper> {
        
        protected EzyPluginsSetting plugins;
        
        public Builder plugins(EzyPluginsSetting plugins) {
            this.plugins = plugins;
            return this;
        }
        
        @Override
        public EzyEventPluginsMapper build() {
            return new EzyEventPluginsMapperImpl(this);
        }
        
        protected EzyMapSet<EzyConstant, EzyPluginSetting> newEventsPluginss() {
            EzyMapSet<EzyConstant, EzyPluginSetting> eventsPluginss = new EzyHashMapSet<>();
            for(EzyPluginSetting p : plugins.getPlugins()) {
                for(EzyConstant e : p.getListenEvents().getEvents()) {
                    eventsPluginss.addItems(e, p);
                }
                eventsPluginss.addItems(EzyEventType.SERVER_READY, p);
                eventsPluginss.addItems(EzyEventType.USER_REQUEST, p);
            }
            return eventsPluginss;
        }
    }
    
}
