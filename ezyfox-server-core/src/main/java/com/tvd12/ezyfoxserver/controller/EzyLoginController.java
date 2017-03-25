package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.entity.impl.EzySimpleUser;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyUserLoginEventImpl;
import com.tvd12.ezyfoxserver.response.EzyLoginResponse;

public class EzyLoginController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzySession> {

	@Override
	public void handle(EzyServerContext ctx, EzySession session, EzyArray data) {
		getLogger().info("begin login handler {}", data);
		doHandle(ctx, session, data);
		getLogger().info("end login handler");
	}
	
	private void doHandle(EzyServerContext ctx, EzySession session, EzyArray data) {
		try {
			process(ctx, newLoginEvent(session, data));
		}
		catch(Exception e) {
			getLogger().error("login error", e);
		}
	}
	
	protected void process(EzyServerContext ctx, EzyUserLoginEvent event) {
		firePluginEvent(ctx, event);
		process(ctx, newUser(event), event);
	}
	
	protected void firePluginEvent(EzyServerContext ctx, EzyUserLoginEvent event) {
		ctx.get(EzyFirePluginEvent.class).fire(EzyEventType.USER_LOGIN, event);
	}
	
	protected void process(EzyServerContext ctx, EzyUser user, EzyUserLoginEvent event) {
		addUser(ctx, user);
		response(ctx, user, event);
	}
	
	protected void addUser(EzyServerContext ctx, EzyUser user) {
		getUserManager(ctx).addUser(user);
	}
	
	protected EzyUser newUser(EzyUserLoginEvent event) {
		EzySimpleUser user = new EzySimpleUser();
		user.setName(event.getUsername());
		user.setSession(event.getSession());
		user.setPassword(event.getPassword());
		return user;
	}
	
	protected void response(EzyContext ctx, EzyUser user, EzyUserLoginEvent event) {
		ctx.get(EzySendMessage.class)
			.sender(user)
			.data(getResponse(ctx, user, event.getOutput()))
			.execute();
	}
	
	protected EzyArray getResponse(EzyContext ctx, EzyUser user, Object out) {
		return serializeToArray(ctx, newResponse(user, out));
	}
	
	protected EzyLoginResponse newResponse(EzyUser user, Object output) {
		return EzyLoginResponse.builder()
				.data(output)
				.userId(user.getId())
				.username(user.getName())
				.build();
	}
	
	protected EzyUserLoginEvent newLoginEvent(EzySession session, EzyArray data) {
		return EzyUserLoginEventImpl.builder()
				.username(data.get(0))
				.password(data.get(1))
				.data(getLoginData(data))
				.session(session)
				.build();
	}
	
	private Object getLoginData(EzyArray data) {
		return data.size() > 2 ? data.get(2) : newObjectBuilder().build();
	}
	
}
