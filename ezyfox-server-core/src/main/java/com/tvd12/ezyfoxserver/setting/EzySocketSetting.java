package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfoxserver.constant.SslType;

public interface EzySocketSetting extends EzyBaseSocketSetting {
    SslType getSslType();

    int getSslHandshakeTimeout();

    boolean isTcpNoDelay();

    int getMaxRequestSize();

    int getConnectionAcceptorThreadPoolSize();

    int getSslConnectionAcceptorThreadPoolSize();

    int getWriterThreadPoolSize();

    default boolean isL4SslActive() {
        return isSslActive() && getSslType() == SslType.L4;
    }

    default boolean isL7SslActive() {
        return isSslActive() && getSslType() == SslType.L7;
    }
}
