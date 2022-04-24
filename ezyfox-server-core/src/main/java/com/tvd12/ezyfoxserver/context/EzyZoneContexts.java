package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.setting.EzyUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public final class EzyZoneContexts {

    private EzyZoneContexts() {}

    public static EzyZoneSetting getZoneSetting(
        EzyZoneContext context
    ) {
        EzyZone zone = context.getZone();
        return zone.getSetting();
    }

    public static EzyZoneUserManager getUserManager(
        EzyZoneContext context
    ) {
        EzyZone zone = context.getZone();
        return zone.getUserManager();
    }

    public static EzyUserManagementSetting getUserManagementSetting(
        EzyZoneContext context
    ) {
        EzyZoneSetting zoneSetting = getZoneSetting(context);
        return zoneSetting.getUserManagement();
    }
}
