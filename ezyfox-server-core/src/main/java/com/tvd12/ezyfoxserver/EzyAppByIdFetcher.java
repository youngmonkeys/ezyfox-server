package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyAppSetting;

public interface EzyAppByIdFetcher {

    EzyAppSetting getAppById(Integer id);
    
}
