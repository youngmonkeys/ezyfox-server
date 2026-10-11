package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

import java.util.List;

public interface EzyProtocolsSetting extends EzyToMap {

    int getWriterThreadPoolSize();

    List<EzyProtocolSetting> getProtocols();

    List<EzyProtocolSetting> getActiveProtocols();

    default boolean hasActiveProtocol() {
        return !getActiveProtocols().isEmpty();
    }
}
