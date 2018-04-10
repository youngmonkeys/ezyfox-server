package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.function.Consumer;

import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.setting.EzyUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public final class EzyZoneContexts {

    private EzyZoneContexts() {
    }
    
    public static int getZoneId(EzyZoneContext context) {
        return getZoneSetting(context).getId();
    }
    
    public static EzyZoneSetting getZoneSetting(EzyZoneContext context) {
        return context.getZone().getSetting();
    }
    
    public static boolean containsUser(EzyAppContext context, EzyUser user) {
        return EzyAppContexts.containsUser(context, user);
    }
    
    public static boolean containsUser(EzyAppContext context, String username) {
        return EzyAppContexts.containsUser(context, username);
    }
    
    public static void forEachAppContexts(
            EzyZoneContext context, Consumer<EzyAppContext> consumer) {
        for(int appId : getAppIds(context))
            consumer.accept(context.getAppContext(appId));
    }
    
    public static Collection<Integer> getAppIds(EzyZoneContext context) {
        return getZoneSetting(context).getAppIds();
    }

    public static EzyZoneUserManager getUserManager(EzyZoneContext context) {
        return context.getZone().getUserManager();
    }
    
    public static EzyUserManagementSetting getUserManagementSetting(EzyZoneContext context) {
        return getZoneSetting(context).getUserManagement();
    }
    
    public static void handleException(EzyZoneContext context, Thread thread, Throwable throwable) {
        context.get(EzyHandleException.class).handle(thread, throwable);
    }
    
}
