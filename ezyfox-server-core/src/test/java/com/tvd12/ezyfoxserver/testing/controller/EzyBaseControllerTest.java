package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public abstract class EzyBaseControllerTest extends BaseCoreTest {

    protected EzyServer getServer(EzyServerContext ctx) {
        return ctx.getServer();
    }

    protected EzyZoneUserManager getUserManager(EzyZoneContext ctx) {
        return ctx.getZone().getUserManager();
    }

    protected MyTestSessionManager getSessionManager(EzyServerContext ctx) {
        return (MyTestSessionManager) ctx.getServer().getSessionManager();
    }

    protected abstract EzyConstant getCommand();
}
