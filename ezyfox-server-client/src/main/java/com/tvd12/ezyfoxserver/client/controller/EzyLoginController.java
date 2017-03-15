package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.request.EzyAccessAppRequest;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.controller.EzyServerController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.entity.impl.EzySimpleUser;

public class EzyLoginController 
		extends EzyAbstractController 
		implements EzyServerController<EzySession> {

	@Override
	public void handle(EzyContext ctx, EzySession session, EzyArray data) {
		setMe(session, data);
		sendAccessAppRequest(ctx);
	}
	
	protected void sendAccessAppRequest(EzyContext ctx) {
		ctx.get(EzySendMessage.class)
			.data(newAccessAppData(ctx))
			.sender(getSingleton().getMe())
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
	
	protected void setMe(EzySession session, EzyArray data) {
		getSingleton().setMe(createMe(session, data));
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
