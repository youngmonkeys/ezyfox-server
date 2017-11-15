package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.function.Consumer;

import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.setting.EzyHttpSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public final class EzyContexts {

    private EzyContexts() {
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
    
    public static EzyServerUserManager getUserManager(EzyServerContext context) {
        return getManagers(context).getManager(EzyServerUserManager.class);
    }
    
    public static EzyStatistics getStatistics(EzyServerContext context) {
        return context.getServer().getStatistics();
    }
    
    public static void forEachAppContexts(
            EzyServerContext context, Consumer<EzyAppContext> consumer) {
        for(int appId : getAppIds(context))
            consumer.accept(context.getAppContext(appId));
    }
    
    public static Collection<Integer> getAppIds(EzyServerContext context) {
        return context.getServer().getAppIds();
    }
    
    public static EzyHttpSetting getHttpSetting(EzyServerContext context) {
        return getSettings(context).getHttp();
    }
    
    public static void handleException(
            EzyServerContext context, Thread thread, Throwable throwable) {
        context.get(EzyHandleException.class).handle(thread, throwable);
    }
    
}
