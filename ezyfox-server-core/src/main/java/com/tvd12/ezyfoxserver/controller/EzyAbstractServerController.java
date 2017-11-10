package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyServerUserManager;

public class EzyAbstractServerController extends EzyAbstractController {

	protected EzyServer getServer(EzyServerContext ctx) {
		return ctx.getServer();
	}
	
	protected EzySettings getSettings(EzyServerContext ctx) {
	    return getServer(ctx).getSettings();
	}
	
	protected EzyManagers getManagers(EzyServerContext ctx) {
		return getServer(ctx).getManagers();
	}
	
	protected EzyServerUserManager getUserManager(EzyServerContext ctx) {
		return getManagers(ctx).getManager(EzyServerUserManager.class);
	}
	
	@SuppressWarnings("unchecked")
	protected EzySessionManager<EzySession> getSessionManager(EzyServerContext ctx) {
		return getManagers(ctx).getManager(EzySessionManager.class);
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
	
}
