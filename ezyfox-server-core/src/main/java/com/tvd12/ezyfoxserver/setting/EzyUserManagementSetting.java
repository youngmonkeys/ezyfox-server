package com.tvd12.ezyfoxserver.setting;

public interface EzyUserManagementSetting {

    long getUserMaxIdleTime();
    
    int getMaxSessionPerUser();
    
    boolean isAllowGuestLogin();
    
    String getGuestNamePrefix();
    
    String getUserNamePattern();
    
}
