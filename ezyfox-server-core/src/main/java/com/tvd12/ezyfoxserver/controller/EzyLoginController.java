package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.entity.impl.EzySimpleUser;
import com.tvd12.ezyfoxserver.response.EzyLoginResponse;

public class EzyLoginController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzySession> {

	@Override
	public void handle(EzyContext ctx, EzySession session, EzyArray data) {
		getLogger().info("begin login handler {}", data);
		process(ctx, session, newInOutData(data));
		getLogger().info("end login handler");
	}
	
	protected EzyArray newInOutData(EzyArray data) {
		return newArrayBuilder()
				.append(data.duplicate())
				.append(newObjectBuilder())
				.build();
	}
	
	protected void process(EzyContext ctx, EzySession session, EzyArray inout) {
		process(ctx, newUser(session, inout.get(0)), inout.get(1));
	}
	
	protected void process(EzyContext ctx, EzyUser user, EzyObject out) {
		addUser(ctx, user);
		response(ctx, user, out);
	}
	
	protected void addUser(EzyContext ctx, EzyUser user) {
		getUserManager(ctx).addUser(user);
	}
	
	protected EzyUser newUser(EzySession session, EzyArray data) {
		String username = data.get(0);
		String password = data.get(1);
		EzySimpleUser user = new EzySimpleUser();
		user.setName(username);
		user.setSession(session);
		user.setPassword(password);
		return user;
	}
	
	protected void response(EzyContext ctx, EzyUser user, EzyData out) {
		ctx.get(EzySendMessage.class)
			.sender(user)
			.data(getResponse(ctx, user, out))
			.execute();
	}
	
	protected EzyArray getResponse(EzyContext ctx, EzyUser user, EzyData out) {
		return serializeToArray(ctx, newResponse(user, out));
	}
	
	protected EzyLoginResponse newResponse(EzyUser user, EzyData out) {
		return EzyLoginResponse.builder()
				.data(out)
				.userId(user.getId())
				.username(user.getName())
				.build();
	}

}
