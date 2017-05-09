package com.tvd12.ezyfoxserver.setting;

import java.util.Set;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

public interface EzyPluginSetting extends EzyBaseSetting {

    int getPriority();
    
    EzyListenEvents getListenEvents();
    
    interface EzyListenEvents {
        
        Set<EzyConstant> getEvents();
        
    }
    
}
