package com.tvd12.ezyfoxserver.setting;

public interface EzySocketSetting extends EzyBaseSocketSetting {

    boolean isTcpNoDelay();
    
    int getMaxRequestSize();

}
