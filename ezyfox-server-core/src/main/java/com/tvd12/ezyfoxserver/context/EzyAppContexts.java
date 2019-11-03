package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public final class EzyAppContexts {

    private EzyAppContexts() {}
    
    public static EzyUserManager getUserManager(EzyAppContext context) {
        EzyApplication app = context.getApp();
        EzyAppUserManager userManager = app.getUserManager();
        return userManager;
    }
    
    public static boolean containsUser(EzyAppContext context, EzyUser user) {
        EzyUserManager userManager = getUserManager(context);
        boolean contains = userManager.containsUser(user);
        return contains;
    }
    
    public static boolean containsUser(EzyAppContext context, String username) {
        EzyUserManager userManager = getUserManager(context);
        boolean contains = userManager.containsUser(username);
        return contains;
    }
    
}
