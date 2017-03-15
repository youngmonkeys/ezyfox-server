package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzyFetchAppByName;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.config.EzyApp;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.response.EzyAccessAppResponse;

public class EzyAccessAppController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyUser> {

	@Override
	public void handle(EzyContext ctx, EzyUser user, EzyArray data) {
		getLogger().info("begin access app handler {}", data);
		process(ctx, user, data);
		getLogger().info("end access app handler");
	}
	
	protected void process(EzyContext ctx, EzyUser user, EzyArray data) {
		String appName = data.get(0);
		EzyApp app = ctx.get(EzyFetchAppByName.class).get(appName);
		response(ctx, user, getAccessAppResponse(ctx, app, newAccessAppData()));
	}
	
	protected void response(EzyContext ctx, EzyUser user, EzyData out) {
		ctx.get(EzySendMessage.class).sender(user).data(out).execute();
	}
	
	protected EzyObject newAccessAppData() {
		return newObjectBuilder().build();
	}
	
	protected EzyArray getAccessAppResponse(EzyContext ctx, EzyApp app, EzyData out) {
		return serializeToArray(ctx, newAccessAppData(app, out));
	}
	
	protected EzyAccessAppResponse newAccessAppData(EzyApp app, EzyData out) {
		return EzyAccessAppResponse.builder().data(out).appId(app.getId()).build();
	}

}
