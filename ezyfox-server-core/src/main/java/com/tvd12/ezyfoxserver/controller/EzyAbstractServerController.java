package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyServerContexts;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContexts;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public class EzyAbstractServerController extends EzyAbstractController {

	protected EzyServer getServer(EzyServerContext ctx) {
		return ctx.getServer();
	}
	
	protected EzySettings getSettings(EzyServerContext ctx) {
	    return EzyServerContexts.getSettings(ctx);
	}
	
	protected EzyZoneUserManager getUserManager(EzyZoneContext ctx) {
		return EzyZoneContexts.getUserManager(ctx);
	}
	
	protected EzySessionManager<EzySession> getSessionManager(EzyServerContext ctx) {
		return EzyServerContexts.getSessionManager(ctx);
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
	
}
