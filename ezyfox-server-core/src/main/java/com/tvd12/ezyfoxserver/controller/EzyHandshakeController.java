package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyHandshakeEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleHandshakeEvent;
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
	    EzyHandshakeEvent event = newHandshakeEvent(session, params);
	    ctx.handleEvent(EzyEventType.USER_HANDSHAKE, event);
		updateSession(session, event);
		process(ctx, request);
		EzyResponse response = newHandShakeResponse(session, event);
	    ctx.send(response, session, false);
	}
	
	protected void process(EzyServerContext ctx, EzyHandShakeRequest request) {
	    EzySession session = request.getSession();
	    EzyHandshakeParams params = request.getParams();
	    String reconnectToken = params.getReconnectToken();
	    ((EzyAbstractSession)session).setBeforeToken(reconnectToken);
	}
	
	protected void updateSession(EzySession session, EzyHandshakeEvent event) {
		session.setClientId(event.getClientId());
		session.setClientKey(event.getClientKey());
		session.setClientType(event.getClientType());
		session.setClientVersion(event.getClientVersion());
		session.setSessionKey(event.getSessionKey());
	}
	
	protected EzyHandshakeEvent newHandshakeEvent(
			EzySession session, EzyHandshakeParams params) {
		return new EzySimpleHandshakeEvent(
		        session,
		        params.getClientId(),
		        params.getClientKey(),
		        params.getClientType(), 
		        params.getClientVersion(),
		        params.getReconnectToken(),
		        params.isEnableEncryption());
	}
	
	protected EzyResponse newHandShakeResponse(
			EzySession session, EzyHandshakeEvent event) {
	    EzyHandShakeParams params = new EzyHandShakeParams();
	    params.setServerPublicKey(session.getPublicKey());
	    params.setReconnectToken(session.getToken());
	    params.setSessionId(session.getId());
	    params.setSessionKey(event.getEncryptedSessionKey());
	    return new EzyHandShakeResponse(params);
	}
	
}
