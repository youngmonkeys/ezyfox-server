package com.tvd12.ezyfoxserver.setting;

public interface EzyWebSocketSetting extends EzyBaseSocketSetting {

    int getSslPort();

    int getMaxFrameSize();

    int getMinHandlerThreadPoolSize();

    int getMaxHandlerThreadPoolSize();

    int getWriterThreadPoolSize();

    EzySslConfigSetting getSslConfig();

    boolean isManagementEnable();
}
