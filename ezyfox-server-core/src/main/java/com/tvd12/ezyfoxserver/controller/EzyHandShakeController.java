package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyHandShakeResponse;

public class EzyHandShakeController 
		extends EzyAbstractController 
		implements EzyServerController<EzySession> {

	@Override
	public void handle(EzyContext ctx, EzySession session, EzyArray data) {
		String token = data.get(0);
		getLogger().info("begin hanshake handler, token = {}", token);
		response(ctx, session);
		getLogger().info("end hanshake handler, token = {}", session.getToken());
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
