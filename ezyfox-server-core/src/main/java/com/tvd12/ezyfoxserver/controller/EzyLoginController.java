package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyHandShakeResponse;

public class EzyLoginController 
		extends EzyAbstractController 
		implements EzyServerController {

	@Override
	public void handle(EzyContext ctx, EzySession session, EzyArray data) {
		String username = data.get(0);
		String password = data.get(1);
		EzyObject indata = data.get(2);
		getLogger().info("begin login handler {}", data);
//		response(ctx, session);
		getLogger().info("end login handler");
	}
	
	protected void response(EzyContext ctx, EzySession session) {
		ctx.get(EzySendMessage.class)
			.data(getResponse(ctx, session))
			.sender(session)
			.execute();
	}
	
	protected EzyArray getResponse(EzyContext ctx, EzySession session) {
		return serializeToArray(ctx, newResponse(session));
	}
	
	protected EzyHandShakeResponse newResponse(EzySession session) {
		return EzyHandShakeResponse.builder()
				.token(session.getToken())
				.build();
	}

}
