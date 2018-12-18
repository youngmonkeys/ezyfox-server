package com.tvd12.ezyfoxserver.setting;

import java.util.List;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyEventControllersSetting extends EzyToMap {

    List<EzyEventControllerSetting> getEventControllers();
    
}
