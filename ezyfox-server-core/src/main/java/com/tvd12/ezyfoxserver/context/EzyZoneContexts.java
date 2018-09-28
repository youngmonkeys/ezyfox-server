package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.setting.EzyUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public final class EzyZoneContexts {

    private EzyZoneContexts() {
    }
    
    public static EzyZoneSetting getZoneSetting(EzyZoneContext context) {
        return context.getZone().getSetting();
    }
    
    public static EzyZoneUserManager getUserManager(EzyZoneContext context) {
        return context.getZone().getUserManager();
    }
    
    public static EzyUserManagementSetting getUserManagementSetting(EzyZoneContext context) {
        return getZoneSetting(context).getUserManagement();
    }
    
}
