package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public final class EzyAppContexts {

    private EzyAppContexts() {
    }
    
    public static EzyUserManager getUserManager(EzyAppContext context) {
        return context.getApp().getUserManager();
    }
    
    public static boolean containsUser(EzyAppContext context, EzyUser user) {
        return getUserManager(context).containsUser(user);
    }
    
    public static boolean containsUser(EzyAppContext context, String username) {
        return getUserManager(context).containsUser(username);
    }
    
}
