package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public abstract class EzyBaseControllerTest extends BaseCoreTest {

    protected static final String RECONNECT_TOKEN_SALT = "$1$reconnectToken";
    
    protected EzyServer getServer(EzyServerContext ctx) {
        return ctx.getServer();
    }
    
    protected EzyManagers getManagers(EzyServerContext ctx) {
        return getServer(ctx).getManagers();
    }
    
    protected EzyZoneUserManager getUserManager(EzyServerContext ctx) {
        return getManagers(ctx).getManager(EzyZoneUserManager.class);
    }
    
    protected MyTestSessionManager getSessionManager(EzyServerContext ctx) {
        return (MyTestSessionManager) getManagers(ctx).getManager(EzySessionManager.class);
    }
    
    protected EzyAppContext getAppContext(EzyServerContext ctx, int appId) {
        return ctx.getAppContext(appId);
    }
    
    protected EzyAppContext getAppContext(EzyServerContext ctx, String appName) {
        return ctx.getAppContext(appName);
    }
    
    protected EzyServerControllers getControllers(EzyServerContext ctx) {
        return getServer(ctx).getControllers();
    }
    
    @SuppressWarnings("rawtypes")
    protected EzyController getController(EzyServerContext ctx, EzyConstant cmd) {
        return getServer(ctx).getControllers().getController(cmd);
    }
    
    protected abstract EzyConstant getCommand();
    
}
