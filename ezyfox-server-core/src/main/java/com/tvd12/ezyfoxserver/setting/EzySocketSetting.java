package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfoxserver.constant.SslType;

public interface EzySocketSetting extends EzyBaseSocketSetting {
    SslType getSslType();

    boolean isTcpNoDelay();

    int getMaxRequestSize();

    int getWriterThreadPoolSize();
}
