package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.client.context.EzyClientAppContext;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.context.EzySimpleClientAppContext;
import com.tvd12.ezyfoxserver.client.entity.EzyClientUser;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class EzyAccessAppController 
		extends EzyAbstractController 
		implements EzyClientController<EzyClientUser> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void handle(EzyClientContext ctx, EzyClientUser user, EzyArray data) {
		getLogger().info("access app success, data = " + data);
		EzyClientAppContext appCtx = newAppContext(ctx, data);
		ctx.addAppContext(appCtx);
		EzyClient client = ctx.getClient();
		EzyClientAppController ctrl = client.getAppController(EzyClientCommand.ACESS_APP_SUCCESS);
		if(ctrl != null) ctrl.handle(appCtx, user, data.get(2, EzyArray.class));
	}
	
	protected EzyClientAppContext newAppContext(EzyClientContext ctx, EzyArray data) {
		return EzySimpleClientAppContext.builder()
				.appId(data.get(0, int.class))
				.appName(data.get(1, String.class))
				.parent(ctx)
				.build();
	}
	
	
}
