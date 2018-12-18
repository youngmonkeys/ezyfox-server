package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyEventControllerSetting extends EzyToMap {

    String getEventType();
    
    String getController();
    
}
