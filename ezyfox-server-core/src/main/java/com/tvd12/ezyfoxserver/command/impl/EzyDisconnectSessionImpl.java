package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyDisconnectSession;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyMessageController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyDisconnectResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public class EzyDisconnectSessionImpl 
		extends EzyMessageController 
		implements EzyDisconnectSession {

    private EzySession session;
	private EzyConstant reason;
	private boolean fireClientEvent = true;
	
	private EzyServerContext context;
	
	public EzyDisconnectSessionImpl(EzyServerContext ctx) {
		this.context = ctx;
	}
	
	@Override
	public Boolean execute() {
		sendToClients();
		disconnectSession();
		return Boolean.TRUE;
	}
	
	protected void sendToClients() {
		if(fireClientEvent)
			doSendToClient();
	}
	
	protected void disconnectSession() {
        getLogger().info("disconnect session: {}, reason: {}", session.getClientAddress(), reason);
        session.disconnect();
        session.close();
    }
	
	protected void doSendToClient() {
	    responseNow(context, session, newResponse());
	}
	
	protected EzyResponse newResponse() {
        return EzyDisconnectResponse.builder().reason(reason).build();
    }

	@Override
	public EzyDisconnectSession session(EzySession session) {
		this.session = session;
		return this;
	}

	@Override
	public EzyDisconnectSession reason(EzyConstant reason) {
		this.reason = reason;
		return this;
	}

	@Override
	public EzyDisconnectSession fireClientEvent(boolean value) {
		this.fireClientEvent = value;
		return this;
	}

}
