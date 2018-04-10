package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.setting.EzyHttpSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public final class EzyServerContexts {

    private EzyServerContexts() {
    }
    
    public static EzySettings getSettings(EzyServerContext context) {
        return context.getServer().getSettings();
    }
    
    public static boolean containsUser(EzyAppContext context, EzyUser user) {
        return EzyAppContexts.containsUser(context, user);
    }
    
    public static boolean containsUser(EzyAppContext context, String username) {
        return EzyAppContexts.containsUser(context, username);
    }
    
    public static EzyManagers getManagers(EzyServerContext context) {
        return context.getServer().getManagers();
    }
    
    public static EzyUserManager getUserManager(EzyAppContext context) {
        return EzyAppContexts.getUserManager(context);
    }
    
    public static EzyZoneUserManager getUserManager(EzyServerContext context) {
        return getManagers(context).getManager(EzyZoneUserManager.class);
    }
    
    public static EzyStatistics getStatistics(EzyServerContext context) {
        return context.getServer().getStatistics();
    }
    
    @SuppressWarnings("unchecked")
    public static EzySessionManager<EzySession> getSessionManager(EzyServerContext ctx) {
        return getManagers(ctx).getManager(EzySessionManager.class);
    }
    
    public static EzyHttpSetting getHttpSetting(EzyServerContext context) {
        return getSettings(context).getHttp();
    }
    
    public static void handleException(
            EzyServerContext context, Thread thread, Throwable throwable) {
        context.get(EzyHandleException.class).handle(thread, throwable);
    }
    
}
