package com.tvd12.ezyfoxserver.setting;

public interface EzyBaseSocketSetting {

    int getPort();
    
    String getAddress();
    
    boolean isActive();
    
    String getCodecCreator();
    
}
