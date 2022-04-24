package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyHttpSetting extends EzyToMap {

    int getPort();

    boolean isActive();

    int getMaxThreads();
}
