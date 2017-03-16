package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
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
}
