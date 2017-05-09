package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;

public interface EzyAppFetcher {

    EzySimpleAppSetting getAppById(Integer id);
    
    EzySimpleAppSetting getAppByName(String name);
    
}
