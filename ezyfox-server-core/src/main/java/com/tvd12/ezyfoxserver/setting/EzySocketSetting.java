package com.tvd12.ezyfoxserver.setting;

public interface EzySocketSetting extends EzyBaseSocketSetting {

    boolean isTcpNoDelay();
    
    boolean isSslActive();
    
    int getMaxRequestSize();
    
    int getWriterThreadPoolSize();
}
