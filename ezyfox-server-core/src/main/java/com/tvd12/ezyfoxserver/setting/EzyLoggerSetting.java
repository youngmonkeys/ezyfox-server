package com.tvd12.ezyfoxserver.setting;

import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;

public interface EzyLoggerSetting {

    EzyIgnoredCommandsSetting getIgnoredCommands();
    
    interface EzyIgnoredCommandsSetting {
        
        Set<EzyConstant> getCommands();
        
    }
    
}
