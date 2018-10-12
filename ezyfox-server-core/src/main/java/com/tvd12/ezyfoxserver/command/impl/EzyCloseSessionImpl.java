package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyCloseSession;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyMessageController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyDisconnectParams;
import com.tvd12.ezyfoxserver.response.EzyDisconnectResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public class EzyCloseSessionImpl 
		extends EzyMessageController 
		implements EzyCloseSession {

	private final EzyServerContext context;
	
	public EzyCloseSessionImpl(EzyServerContext ctx) {
		this.context = ctx;
	}
	
	@Override
	public void close(EzySession session, EzyConstant reason) {
	    sendToClients(session, reason);
        disconnectSession(session, reason);
	}
	
	protected void sendToClients(EzySession session, EzyConstant reason) {
		if(shouldSendToClient(reason))
		    sendToClients0(session, reason);
	}
	
	protected boolean shouldSendToClient(EzyConstant reason) {
	    return reason != EzyDisconnectReason.UNKNOWN;
	}
	
	protected void disconnectSession(EzySession session, EzyConstant reason) {
        getLogger().info("close session: {}, reason: {}", session.getClientAddress(), reason);
        session.close();
    }
	
	protected void sendToClients0(EzySession session, EzyConstant reason) {
	    EzyResponse response = newResponse(reason);
	    context.send(response, session, true);
	}
	
	protected EzyResponse newResponse(EzyConstant reason) {
	    EzyDisconnectParams params = new EzyDisconnectParams();
	    params.setReason(reason);
        return new EzyDisconnectResponse(params);
    }

}
