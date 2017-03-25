package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyAbstractServerController extends EzyAbstractController {

	protected EzyServer getServer(EzyServerContext ctx) {
		return ctx.getBoss();
	}
	
	protected EzyManagers getManagers(EzyServerContext ctx) {
		return getServer(ctx).getManagers();
	}
	
	protected EzyUserManager getUserManager(EzyServerContext ctx) {
		return getManagers(ctx).getManager(EzyUserManager.class);
	}
	
	protected EzyAppContext getAppContext(EzyServerContext ctx, int appId) {
		return ctx.getAppContext(appId);
	}
	
	protected EzyAppContext getAppContext(EzyServerContext ctx, String appName) {
		return ctx.getAppContext(appName);
	}
	
	protected EzyControllers getControllers(EzyServerContext ctx) {
		return getServer(ctx).getControllers();
	}
	
	@SuppressWarnings("rawtypes")
	protected EzyController getController(EzyServerContext ctx, EzyConstant cmd) {
		return getServer(ctx).getControllers().getController(cmd);
	}
}
