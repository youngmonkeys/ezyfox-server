package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Set;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginsSetting;
import com.tvd12.ezyfoxserver.util.EzyHashMapSet;
import com.tvd12.ezyfoxserver.util.EzyMapSet;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;

public class EzyEventPluginsMapperImpl implements EzyEventPluginsMapper {

    protected final EzyMapSet<EzyConstant, EzyPluginSetting> eventsPluginss;
    
    public EzyEventPluginsMapperImpl() {
        this.eventsPluginss = new EzyHashMapSet<>();
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
            EzyEventPluginsMapperImpl answer = new EzyEventPluginsMapperImpl();
            for(EzyPluginSetting p : plugins.getPlugins()) {
                for(EzyConstant e : p.getListenEvents().getEvents()) {
                    answer.eventsPluginss.addItems(e, p);
                }
                answer.eventsPluginss.addItems(EzyEventType.SERVER_READY, p);
                answer.eventsPluginss.addItems(EzyEventType.USER_REQUEST, p);
            }
            return answer;
        }
    }
    
}
