package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.request.EzyHandshakeParams;
import com.tvd12.ezyfoxserver.request.EzyReconnectRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleReconnectRequest;
import com.tvd12.ezyfoxserver.response.EzyHandShakeResponse;
import com.tvd12.ezyfoxserver.response.EzyHandShakeParams;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.util.EzyEntityArrays;
import com.tvd12.ezyfoxserver.util.EzyIfElse;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzyHandshakeController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyHandShakeRequest> {

    private EzyReconnectController reconnectController = new EzyReconnectController();
    
	@Override
	public void handle(EzyServerContext ctx, EzyHandShakeRequest request) {
	    EzySession session = request.getSession();
	    EzyHandshakeParams params = request.getParams();
		updateSession(session, params);
		process(ctx, request);
	}
	
	protected void process(EzyServerContext ctx, EzyHandShakeRequest request) {
	    EzySessionManagementSetting setting = getSessionManagementSetting(ctx);
	    EzySessionManager<EzySession> sessionManager = getSessionManager(ctx);
	    EzySession newsession = request.getSession();
	    EzyHandshakeParams params = request.getParams();
	    String reconnectToken = params.getReconnectToken();
	    EzySession oldsession = sessionManager.getSession(reconnectToken);
	    boolean allowReconnect = setting.isSessionAllowReconnect();
	    boolean isReconnect = oldsession != null && allowReconnect;
	    EzyIfElse.withIf(isReconnect, ()-> processReconnect(ctx, oldsession, newsession));
	    response(ctx, newsession, isReconnect);
	}
	
	protected void updateSession(EzySession session, EzyHandshakeParams params) {
		session.setClientId(params.getClientId());
		session.setClientKey(EzyBase64.decode(params.getClientKey()));
		session.setClientType(params.getClientType());
		session.setClientVersion(params.getClientVersion());
	}
	
	protected void processReconnect(
	        EzyServerContext ctx, EzySession oldsession, EzySession newsession) {
	    getLogger().info("session {} is reconnect", newsession.getClientAddress());
	    reconnectController.handle(ctx, newReconnectRequest(oldsession, newsession));
	}
	
	protected EzyReconnectRequest 
	        newReconnectRequest(EzySession oldsession, EzySession newsession) {
	    EzySimpleReconnectRequest answer = new EzySimpleReconnectRequest();
	    answer.setSession(newsession);
	    answer.setOldSession(oldsession);
	    answer.deserializeParams(EzyEntityArrays.newArray());
	    return answer;
    }
	
	protected void response(EzyContext ctx, EzySession session, boolean reconnect) {
		response(ctx, session, newHandShakeResponse(session, reconnect));
	}
	
	protected EzyResponse newHandShakeResponse(EzySession session, boolean reconnect) {
	    EzyHandShakeParams params = new EzyHandShakeParams();
	    params.setClientKey(session.getClientKey());
	    params.setServerPublicKey(session.getPublicKey());
	    params.setReconnectToken(session.getReconnectToken());
	    return new EzyHandShakeResponse(params);
	}
	
	protected EzySessionManagementSetting getSessionManagementSetting(EzyServerContext context) {
	    return getSettings(context).getSessionManagement();
	}
	
}
