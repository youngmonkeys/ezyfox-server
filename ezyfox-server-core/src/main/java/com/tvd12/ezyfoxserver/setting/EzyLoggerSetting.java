package com.tvd12.ezyfoxserver.setting;

import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyLoggerSetting extends EzyToMap {

    EzyIgnoredCommandsSetting getIgnoredCommands();
    
    interface EzyIgnoredCommandsSetting {
        
        Set<EzyConstant> getCommands();
        
    }
    
}
