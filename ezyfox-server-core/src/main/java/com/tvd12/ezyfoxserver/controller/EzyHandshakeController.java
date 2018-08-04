package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfox.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.request.EzyHandshakeParams;
import com.tvd12.ezyfoxserver.response.EzyHandShakeParams;
import com.tvd12.ezyfoxserver.response.EzyHandShakeResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public class EzyHandshakeController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyHandShakeRequest> {

	@Override
	public void handle(EzyServerContext ctx, EzyHandShakeRequest request) {
	    EzySession session = request.getSession();
	    EzyHandshakeParams params = request.getParams();
		updateSession(session, params);
		process(ctx, request);
	}
	
	protected void process(EzyServerContext ctx, EzyHandShakeRequest request) {
	    EzySession newsession = request.getSession();
	    EzyHandshakeParams params = request.getParams();
	    String reconnectToken = params.getReconnectToken();
	    ((EzyAbstractSession)newsession).setBeforeReconnectToken(reconnectToken);
	    response(ctx, newsession, newHandShakeResponse(newsession));
	}
	
	protected void updateSession(EzySession session, EzyHandshakeParams params) {
		session.setClientId(params.getClientId());
		session.setClientKey(EzyBase64.decode(params.getClientKey()));
		session.setClientType(params.getClientType());
		session.setClientVersion(params.getClientVersion());
	}
	
	protected EzyResponse newHandShakeResponse(EzySession session) {
	    EzyHandShakeParams params = new EzyHandShakeParams();
	    params.setClientKey(session.getClientKey());
	    params.setServerPublicKey(session.getPublicKey());
	    params.setReconnectToken(session.getReconnectToken());
	    return new EzyHandShakeResponse(params);
	}
	
}
