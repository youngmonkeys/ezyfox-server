package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.function.Consumer;

import com.tvd12.ezyfoxserver.entity.EzyUser;
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
        return getUserManager(context).containsUser(user);
    }
    
    public static boolean containsUser(EzyAppContext context, String username) {
        return getUserManager(context).containsUser(username);
    }
    
    public static EzyManagers getManagers(EzyServerContext context) {
        return context.getServer().getManagers();
    }
    
    public static EzyUserManager getUserManager(EzyAppContext context) {
        return context.getApp().getUserManager();
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
    
}
