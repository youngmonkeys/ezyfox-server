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

    private EzySession session;
	private EzyConstant reason;
	
	private EzyServerContext context;
	
	public EzyCloseSessionImpl(EzyServerContext ctx) {
		this.context = ctx;
	}
	
	@Override
	public Boolean execute() {
		sendToClients();
		disconnectSession();
		return Boolean.TRUE;
	}
	
	protected void sendToClients() {
		if(shouldSendToClient())
		    sendToClients0();
	}
	
	protected boolean shouldSendToClient() {
	    return reason != EzyDisconnectReason.UNKNOWN;
	}
	
	protected void disconnectSession() {
        getLogger().info("close session: {}, reason: {}", session.getClientAddress(), reason);
        session.close();
    }
	
	protected void sendToClients0() {
	    responseNow(context, session, newResponse());
	}
	
	protected EzyResponse newResponse() {
	    EzyDisconnectParams params = new EzyDisconnectParams();
	    params.setReason(reason);
        return new EzyDisconnectResponse(params);
    }

	@Override
	public EzyCloseSession session(EzySession session) {
		this.session = session;
		return this;
	}

	@Override
	public EzyCloseSession reason(EzyConstant reason) {
		this.reason = reason;
		return this;
	}

}
