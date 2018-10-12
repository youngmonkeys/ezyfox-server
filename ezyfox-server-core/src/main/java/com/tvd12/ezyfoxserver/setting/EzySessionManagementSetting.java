package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction;

public interface EzySessionManagementSetting {

    long getSessionMaxIdleTime();
    
    long getSessionMaxWaitingTime();
    
    EzyMaxRequestPerSecond getSessionMaxRequestPerSecond();
    
    interface EzyMaxRequestPerSecond {
        
        int getValue();
        
        EzyMaxRequestPerSecondAction getAction();
    }
    
}
