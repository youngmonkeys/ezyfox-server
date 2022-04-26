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
        return server.getSettings();
    }

    public static boolean containsUser(EzyAppContext context, EzyUser user) {
        return EzyAppContexts.containsUser(context, user);
    }

    public static boolean containsUser(EzyAppContext context, String username) {
        return EzyAppContexts.containsUser(context, username);
    }

    public static EzyUserManager getUserManager(EzyAppContext context) {
        return EzyAppContexts.getUserManager(context);
    }

    public static EzyStatistics getStatistics(EzyServerContext context) {
        EzyServer server = context.getServer();
        return server.getStatistics();
    }

    @SuppressWarnings("unchecked")
    public static EzySessionManager<EzySession> getSessionManager(
        EzyServerContext ctx
    ) {
        EzyServer server = ctx.getServer();
        return (EzySessionManager<EzySession>) server.getSessionManager();
    }
}
