package com.tvd12.ezyfoxserver.command.impl;

import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.containsUser;
import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.forEachAppContexts;

import java.util.ArrayList;
import java.util.List;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyDisconnectUser;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyMessageController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleUserDisconnectEvent;
import com.tvd12.ezyfoxserver.response.EzyDisconnectParams;
import com.tvd12.ezyfoxserver.response.EzyDisconnectResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyDisconnectUserImpl 
		extends EzyMessageController 
		implements EzyDisconnectUser {

	private EzyUser user;
	private EzyConstant reason;
	private boolean fireClientEvent = true;
	private boolean fireServerEvent = true;
	private List<EzySession> sessions = new ArrayList<>();
	
	private EzyZoneContext context;
	
	public EzyDisconnectUserImpl(EzyZoneContext ctx) {
		this.context = ctx;
	}
	
	@Override
	public Boolean execute() {
	    notifyServer();
	    removeUserFromApps();
        sendToClients();
        disconnectSessions();
        destroyUser();
        return Boolean.TRUE;
	}
	
	protected void sendToClients() {
		if(fireClientEvent)
			doSendToClients();
	}
	
	protected void notifyServer() {
		if(fireServerEvent)
			doNotifyServer();
	}
	
	protected void doNotifyServer() {
	    try {
	        context.get(EzyFireAppEvent.class)
		        .filter(appCtxt -> containsUser(appCtxt, user))
		        .fire(EzyEventType.USER_DISCONNECT, newDisconnectEvent());
	    }
	    catch(Exception e) {
	        getLogger().error("notify user disconnect to server error", e);
	    }
	}
	
	protected EzyEvent newDisconnectEvent() {
		return EzySimpleUserDisconnectEvent.builder()
				.user(user)
				.reason(reason)
				.build();
	}
	
	protected void disconnectSessions() {
	    sessions.forEach(this::disconnectSession);
	}
	
	protected void disconnectSession(EzySession session) {
        getLogger().info("disconnect session: {}, reason: {}", session.getClientAddress(), reason);
        session.disconnect();
        session.close();
    }
	
	protected void doSendToClients() {
	    sessions.forEach(this::doSendToClient);
    }
	
	protected void doSendToClient(EzySession session) {
	    responseNow(context, session, newResponse());
	}
	
	protected void destroyUser() {
	    user.destroy();
	}
	
	protected void removeUserFromApps() {
	    forEachAppContexts(context, this::removeUserFromApp);
	}
	
	protected void removeUserFromApp(EzyAppContext ctx) {
	    removeUserFromApp(ctx.getApp().getUserManager());
	}
	
	protected void removeUserFromApp(EzyUserManager userManager) {
	    if(userManager.containsUser(user))
	        userManager.removeUser(user);
	}
	
	protected EzyResponse newResponse() {
	    EzyDisconnectParams params = new EzyDisconnectParams();
	    params.setReason(reason);
        return new EzyDisconnectResponse(params);
    }

	@Override
	public EzyDisconnectUser user(EzyUser user) {
		this.user = user;
		if(user != null)
		    this.sessions.addAll(user.getSessions());
		return this;
	}

	@Override
	public EzyDisconnectUser reason(EzyConstant reason) {
		this.reason = reason;
		return this;
	}

	@Override
	public EzyDisconnectUser fireClientEvent(boolean value) {
		this.fireClientEvent = value;
		return this;
	}

	@Override
	public EzyDisconnectUser fireServerEvent(boolean value) {
		this.fireServerEvent = value;
		return this;
	}
	
}
