package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyFetchServer;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyAbstractServerController extends EzyAbstractController {

	protected EzyServer getServer(EzyContext ctx) {
		return ctx.get(EzyFetchServer.class).get();
	}
	
	protected EzyManagers getManagers(EzyContext ctx) {
		return getServer(ctx).getManagers();
	}
	
	protected EzyUserManager getUserManager(EzyContext ctx) {
		return getManagers(ctx).getManager(EzyUserManager.class);
	}
}
