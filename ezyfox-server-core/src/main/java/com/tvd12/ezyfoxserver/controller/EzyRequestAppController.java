package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyRequestAppEventImpl;

public class EzyRequestAppController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyUser> {

	@Override
	public void handle(EzyServerContext ctx, EzyUser user, EzyArray data) {
		getLogger().info("begin access app handler {}", data);
		process(ctx, user, data);
		getLogger().info("end access app handler");
	}
	
	protected void process(EzyServerContext ctx, EzyUser user, EzyArray data) {
		getLogger().info("process access app, data {}", data);
		EzyAppContext appCtx = ctx.getAppContext(data.get(0, int.class));
		EzyEvent event = newRequestAppEvent(user, data.get(1));
		EzyEventType type = EzyEventType.USER_REQUEST;
		appCtx.get(EzyFireEvent.class).fire(type, event);
	}
	
	protected EzyEvent newRequestAppEvent(EzyUser user, EzyArray params) {
		return EzyRequestAppEventImpl.builder().user(user).data(params).build();
	}
	
}
