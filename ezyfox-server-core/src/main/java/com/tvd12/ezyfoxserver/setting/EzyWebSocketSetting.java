package com.tvd12.ezyfoxserver.setting;

public interface EzyWebSocketSetting extends EzyBaseSocketSetting {

    int getSslPort();
    
    int getMaxFrameSize();
    
    boolean isSslActive();
    
    EzySslConfigSetting getSslConfig();
    
}
