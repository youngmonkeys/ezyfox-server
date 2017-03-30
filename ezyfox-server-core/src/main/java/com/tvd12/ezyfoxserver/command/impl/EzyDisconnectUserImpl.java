package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyDisconnectUser;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyMessageController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyDisconnectEventImpl;
import com.tvd12.ezyfoxserver.response.EzyDisconnectResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public class EzyDisconnectUserImpl 
		extends EzyMessageController 
		implements EzyDisconnectUser {

	private EzyUser user;
	private EzySession session;
	private EzyConstant reason;
	private boolean fireClientEvent = true;
	private boolean fireServerEvent = true;
	
	private EzyServerContext context;
	
	public EzyDisconnectUserImpl(EzyServerContext ctx) {
		this.context = ctx;
	}
	
	@Override
	public Boolean execute() {
		notifyServer();
		sendToClient();
		disconnect();
		return Boolean.TRUE;
	}
	
	protected void sendToClient() {
		if(fireClientEvent)
			doSendToClient();
	}
	
	protected void doSendToClient() {
		context.get(EzySendMessage.class)
			.sender(session)
			.data(getDisconnectData())
			.execute();
	}
	
	protected EzyArray getDisconnectData() {
		return serializeToArray(context, newDisconnectResponse());
	}
	
	protected EzyResponse newDisconnectResponse() {
		return EzyDisconnectResponse.builder()
				.reason(reason)
				.build();
	}
	
	protected void notifyServer() {
		if(shouldNotifyServer())
			doNotifyServer();
	}
	
	protected boolean shouldNotifyServer() {
		return user != null && fireServerEvent;
	}
	
	protected void doNotifyServer() {
		context.get(EzyFireAppEvent.class)
			.fire(EzyEventType.USER_DISCONNECT, newDisconnectEvent());
	}
	
	protected EzyEvent newDisconnectEvent() {
		return EzyDisconnectEventImpl.builder()
				.user(user)
				.reason(reason)
				.build();
	}
	
	protected void disconnect() {
		session.getChannel().disconnect().syncUninterruptibly();
		session.getChannel().close();
	}

	@Override
	public EzyDisconnectUser user(EzyUser user) {
		this.user = user;
		return user != null ? session(user.getSession()) : this;
	}

	@Override
	public EzyDisconnectUser session(EzySession session) {
		this.session = session;
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
		this.fireClientEvent = value;
		return this;
	}
	
}
