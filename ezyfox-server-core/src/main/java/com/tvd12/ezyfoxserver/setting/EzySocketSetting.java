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

    default boolean isCertificationSslActive() {
        return isSslActive() && getSslType() == SslType.CERTIFICATION;
    }

    default boolean isCustomizationSslActive() {
        return isSslActive() && getSslType() == SslType.CUSTOMIZATION;
    }
}
