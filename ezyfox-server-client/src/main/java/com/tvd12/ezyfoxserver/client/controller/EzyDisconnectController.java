package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.EzyClientContext;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public class EzyDisconnectController 
		extends EzyAbstractController 
		implements EzyClientController<Object> {

	@Override
	public void handle(EzyClientContext ctx, Object rev, EzyArray data) {
		if(rev instanceof EzyUser)
			handle(ctx, (EzyUser)rev, data);
		else
			handle(ctx, (EzySession)rev, data);
	}
	
	protected void handle(EzyClientContext ctx, EzyUser user, EzyArray data) {
		handle(ctx, user.getSession(), data);
	}
	
	protected void handle(EzyClientContext ctx, EzySession session, EzyArray data) {
		getLogger().info("user be disconnected by reason {}", 
				EzyDisconnectReason.valueOf(data.get(0, Integer.class)));
	}
	
}
