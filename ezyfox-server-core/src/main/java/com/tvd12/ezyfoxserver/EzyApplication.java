package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

public interface EzyApplication {

    EzyAppSetting getSetting();
    
    EzyAppUserManager getUserManager();
    
}
