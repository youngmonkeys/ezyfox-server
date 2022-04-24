package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public final class EzyAppContexts {

    private EzyAppContexts() {}

    public static EzyUserManager getUserManager(EzyAppContext context) {
        EzyApplication app = context.getApp();
        return app.getUserManager();
    }

    public static boolean containsUser(EzyAppContext context, EzyUser user) {
        EzyUserManager userManager = getUserManager(context);
        return userManager.containsUser(user);
    }

    public static boolean containsUser(EzyAppContext context, String username) {
        EzyUserManager userManager = getUserManager(context);
        return userManager.containsUser(username);
    }
}
