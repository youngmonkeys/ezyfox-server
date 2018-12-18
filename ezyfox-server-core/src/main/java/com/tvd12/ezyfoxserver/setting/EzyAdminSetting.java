package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyAdminSetting extends EzyToMap {

    String getUsername();
    
    String getPassword();
    
    String getApiAccessToken();
}
