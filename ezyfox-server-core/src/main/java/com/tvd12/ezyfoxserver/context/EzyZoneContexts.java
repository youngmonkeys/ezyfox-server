package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.setting.EzyUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public final class EzyZoneContexts {

    private EzyZoneContexts() {
    }
    
    public static EzyZoneSetting getZoneSetting(EzyZoneContext context) {
        EzyZone zone = context.getZone();
        EzyZoneSetting setting = zone.getSetting();
        return setting;
    }
    
    public static EzyZoneUserManager getUserManager(EzyZoneContext context) {
        EzyZone zone = context.getZone();
        EzyZoneUserManager userManager = zone.getUserManager();
        return userManager;
    }
    
    public static EzyUserManagementSetting getUserManagementSetting(EzyZoneContext context) {
        EzyZoneSetting zoneSetting = getZoneSetting(context);
        EzyUserManagementSetting userManagement = zoneSetting.getUserManagement();
        return userManagement;
    }
    
}
