package com.tvd12.ezyfoxserver.setting;

import java.util.List;

public interface EzyAdminsSetting {
    
    List<EzyAdminSetting> getAdmins();

    EzyAdminSetting getAdminByName(String username);
    
    EzyAdminSetting getAdminByApiAccessToken(String token);
    
    boolean containsAdminByName(String username);
    
    boolean containsAdminByApiAccessToken(String token);
    
}
