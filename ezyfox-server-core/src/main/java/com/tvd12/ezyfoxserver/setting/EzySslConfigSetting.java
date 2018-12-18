package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzySslConfigSetting extends EzyToMap {

    String getFile();
    
    String getLoader();
    
    String getContextFactoryBuilder();
    
}
