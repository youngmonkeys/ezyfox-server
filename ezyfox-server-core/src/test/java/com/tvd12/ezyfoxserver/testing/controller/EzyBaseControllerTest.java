package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public abstract class EzyBaseControllerTest extends BaseCoreTest {

    protected static final String RECONNECT_TOKEN_SALT = "$1$reconnectToken";
    
    protected EzyServer getServer(EzyServerContext ctx) {
        return ctx.getServer();
    }
    
    protected EzyZoneUserManager getUserManager(EzyZoneContext ctx) {
        return ctx.getZone().getUserManager();
    }
    
    @SuppressWarnings("rawtypes")
    protected MyTestSessionManager getSessionManager(EzyServerContext ctx) {
        return (MyTestSessionManager)(EzySessionManager) ctx.getServer().getSessionManager();
    }
    
    protected EzyAppContext getAppContext(EzyServerContext ctx, int appId) {
        return ctx.getAppContext(appId);
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
