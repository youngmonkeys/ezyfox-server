package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.EzyClientContext;
import com.tvd12.ezyfoxserver.client.request.EzyAccessAppRequest;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.constants.EzyClientConstant;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public class EzyLoginController 
		extends EzyAbstractController 
		implements EzyClientController<EzySession> {

	@Override
	public void handle(EzyClientContext ctx, EzySession session, EzyArray data) {
		setMe(ctx, session, data);
		sendAccessAppRequest(ctx);
	}
	
	protected void sendAccessAppRequest(EzyContext ctx) {
		ctx.get(EzySendMessage.class)
			.data(newAccessAppData(ctx))
			.sender(getMe(ctx))
			.execute();
	}
	
	protected EzyArray newAccessAppData(EzyContext ctx) {
		return serializeToArray(ctx, newAccessAppRequest());
	}
	
	protected EzyAccessAppRequest newAccessAppRequest() {
		return EzyAccessAppRequest.builder()
				.appName("ezyfox-chat")
				.data(newAccessAppData())
				.build();
	}
	
	protected EzyObject newAccessAppData() {
		return newObjectBuilder().build();
	}
	
	protected void setMe(EzyContext ctx, EzySession session, EzyArray data) {
		setProperty(ctx, EzyClientConstant.ME, createMe(session, data));
	}
	
	protected EzyUser createMe(EzySession session, EzyArray data) {
		long userId = data.get(0, long.class);
		String username = data.get(1);
		EzySimpleUser me = new EzySimpleUser();
		me.setId(userId);
		me.setSession(session);
		me.setName(username);
		getLogger().info("login success userId = {}, username = {}", userId, username);
		return me;
	}
	
	protected void sendAccessAppRequest() {
		
	}

}
