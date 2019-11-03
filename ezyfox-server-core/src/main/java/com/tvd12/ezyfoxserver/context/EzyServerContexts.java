package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public final class EzyServerContexts {

    private EzyServerContexts() {}
    
    public static EzySettings getSettings(EzyServerContext context) {
        EzyServer server = context.getServer();
        EzySettings settings = server.getSettings();
        return settings;
    }
    
    public static boolean containsUser(EzyAppContext context, EzyUser user) {
        boolean contains = EzyAppContexts.containsUser(context, user);
        return contains;
    }
    
    public static boolean containsUser(EzyAppContext context, String username) {
        boolean contains = EzyAppContexts.containsUser(context, username);
        return contains;
    }
    
    public static EzyUserManager getUserManager(EzyAppContext context) {
        EzyUserManager userManager = EzyAppContexts.getUserManager(context);
        return userManager;
    }
    
    public static EzyStatistics getStatistics(EzyServerContext context) {
        EzyServer server = context.getServer();
        EzyStatistics statistics = server.getStatistics();
        return statistics;
    }
    
    @SuppressWarnings("unchecked")
    public static EzySessionManager<EzySession> getSessionManager(EzyServerContext ctx) {
        EzyServer server = ctx.getServer();
        EzySessionManager<EzySession> sessionManager = server.getSessionManager();
        return sessionManager;
    }
    
}
