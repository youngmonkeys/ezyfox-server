package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.cmd.EzySendRequest;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.entity.EzyClientSession;
import com.tvd12.ezyfoxserver.client.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class EzyConnectSuccessController 
		extends EzyAbstractController 
		implements EzyClientController<EzyClientSession> {

	@Override
	public void handle(EzyClientContext ctx, EzyClientSession session, EzyArray data) {
		sendHandShakeRequest(ctx, session);
	}
	
	protected void sendHandShakeRequest(EzyClientContext ctx, EzyClientSession session) {
		ctx.get(EzySendRequest.class)
			.sender(session)
    		.request(newHandShakeRequest())
    		.execute();
    }
	
	protected EzyHandShakeRequest newHandShakeRequest() {
    	return EzyHandShakeRequest.builder().build();
    }
	
}
