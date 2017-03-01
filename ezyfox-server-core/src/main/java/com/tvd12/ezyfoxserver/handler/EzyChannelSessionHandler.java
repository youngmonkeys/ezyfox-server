package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfoxserver.entity.EzySession;

public class EzyChannelSessionHandler extends EzyChannelDataHandler {

	@Override
	protected void sessionAdded(EzySession session) {
		getLogger().info("add session: {}", session);
	}

	@Override
	protected void dataReceived(EzySession session, Object data) {
		
	}

}
