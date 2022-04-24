package com.tvd12.ezyfoxserver.setting;

public interface EzyUdpSetting extends EzyBaseSocketSetting {

    int getMaxRequestSize();

    int getChannelPoolSize();

    int getHandlerThreadPoolSize();

}
