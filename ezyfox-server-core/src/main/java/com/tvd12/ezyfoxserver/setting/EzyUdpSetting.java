package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyUdpSetting extends EzyToMap {

    int getPort();

    String getAddress();

    boolean isActive();

    int getMaxRequestSize();

    int getChannelPoolSize();

    int getHandlerThreadPoolSize();
}
