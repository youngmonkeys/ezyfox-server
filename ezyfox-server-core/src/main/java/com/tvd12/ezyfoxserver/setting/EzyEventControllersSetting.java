package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

import java.util.List;

public interface EzyEventControllersSetting extends EzyToMap {

    List<EzyEventControllerSetting> getEventControllers();
}
