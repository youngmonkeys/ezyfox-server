package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyUserManagementSetting extends EzyToMap {

    long getUserMaxIdleTime();

    int getMaxSessionPerUser();

    boolean isAllowGuestLogin();

    String getGuestNamePrefix();

    String getUserNamePattern();

    boolean isAllowChangeSession();

}
