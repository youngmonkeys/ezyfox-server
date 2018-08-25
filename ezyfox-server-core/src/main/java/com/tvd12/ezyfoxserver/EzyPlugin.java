package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

public interface EzyPlugin extends EzyServerChild {

    EzyPluginSetting getSetting();
    
}
