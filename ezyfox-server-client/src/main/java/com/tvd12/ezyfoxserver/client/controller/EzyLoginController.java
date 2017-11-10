package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.client.context.EzyClientAppContext;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.context.EzySimpleClientAppContext;
import com.tvd12.ezyfoxserver.client.entity.EzyClientSession;
import com.tvd12.ezyfoxserver.client.entity.EzySimpleClientUser;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class EzyLoginController 
		extends EzyAbstractController 
		implements EzyClientController<EzyClientSession> {

	@Override
	public void handle(EzyClientContext ctx, EzyClientSession session, EzyArray data) {
		updateMe(ctx, data);
		EzyArray joinedApps = data.get(2, EzyArray.class);
		if(joinedApps.isEmpty())
			processNotReconnect(ctx, session, data);
		else
			processReconnect(ctx, joinedApps);
	}
	
	protected void processNotReconnect(EzyClientContext ctx, EzyClientSession session, EzyArray data) {
	}
	
	protected void updateMe(EzyClientContext ctx, EzyArray data) {
		long userId = data.get(0, long.class);
		String username = data.get(1, String.class);
		EzySimpleClientUser me = (EzySimpleClientUser) ctx.getMe();
		me.setId(userId);
		me.setName(username);
		getLogger().info("login success userId = {}, username = {}", userId, username);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void processReconnect(EzyClientContext ctx, EzyArray joinedApps) {
		for(int i = 0 ; i < joinedApps.size() ; i++) {
			EzyArray data = joinedApps.get(0, EzyArray.class);
			EzyClientAppContext appCtx = newAppContext(ctx, data);
			ctx.addAppContext(appCtx);
			EzyClient client = ctx.getClient();
			EzyClientAppController ctrl = client.getAppController(EzyClientCommand.ACESS_APP_SUCCESS);
			if(ctrl != null) ctrl.handle(appCtx, appCtx.getMe(), newArrayBuilder().build());
		}
	}
	
	protected EzyClientAppContext newAppContext(EzyClientContext ctx, EzyArray data) {
		return EzySimpleClientAppContext.builder()
				.appId(data.get(0, int.class))
				.appName(data.get(1, String.class))
				.parent(ctx)
				.build();
	}
	
}
