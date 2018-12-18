package com.tvd12.ezyfoxserver.setting;

import java.util.List;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyAdminsSetting extends EzyToMap {
    
    List<EzyAdminSetting> getAdmins();

    EzyAdminSetting getAdminByName(String username);
    
    EzyAdminSetting getAdminByApiAccessToken(String token);
    
    boolean containsAdminByName(String username);
    
    boolean containsAdminByApiAccessToken(String token);
    
}
