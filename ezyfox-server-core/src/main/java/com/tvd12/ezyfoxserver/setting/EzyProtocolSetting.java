package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

import java.util.Map;

public interface EzyProtocolSetting extends EzyToMap {

    String getName();

    boolean isActive();

    String getAddress();

    int getPort();

    boolean isSslActive();

    int getMaxRequestSize();

    String getServerCreator();

    String getConfigFile();

    Map<String, String> getProperties();
}
