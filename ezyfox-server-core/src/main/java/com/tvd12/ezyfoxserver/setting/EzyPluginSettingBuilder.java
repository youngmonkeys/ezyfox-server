package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting.EzySimpleListenEvents;

public class EzyPluginSettingBuilder extends EzyAbstractSettingBuilder<
        EzySimplePluginSetting, EzyPluginSettingBuilder> {

    protected int priority;
    protected EzySimpleListenEvents listenEvents = new EzySimpleListenEvents();
    
    public EzyPluginSettingBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }
    
    public EzyPluginSettingBuilder addListenEvent(String event) {
        this.listenEvents.setEvent(event);
        return this;
    }
    
    public EzyPluginSettingBuilder addListenEvent(EzyConstant event) {
        this.listenEvents.setEvent(event);
        return this;
    }
    
    public EzyPluginSettingBuilder listenEvents(EzySimpleListenEvents listenEvents) {
        this.listenEvents = listenEvents;
        return this;
    }
    
    @Override
    protected EzySimplePluginSetting newSetting() {
        EzySimplePluginSetting setting = new EzySimplePluginSetting();
        setting.setPriority(priority);
        setting.setListenEvents(listenEvents);
        return setting;
        
    }
    
}
