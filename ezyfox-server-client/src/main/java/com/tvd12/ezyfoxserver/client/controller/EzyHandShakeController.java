package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.entity.EzyClientSession;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class EzyHandShakeController 
		extends EzyAbstractController 
		implements EzyClientController<EzyClientSession> {

	@Override
	public void handle(EzyClientContext ctx, EzyClientSession session, EzyArray data) {
		updateSession(session, data);
	}
	
	protected void updateSession(EzyClientSession session, EzyArray data) {
		session.setReconnectToken("");
	}
	
}
