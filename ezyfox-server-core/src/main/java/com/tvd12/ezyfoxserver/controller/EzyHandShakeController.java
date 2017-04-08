package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyHandShakeResponse;
import com.tvd12.ezyfoxserver.sercurity.EzyAsyCrypt;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;

public class EzyHandShakeController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzySession> {

	@Override
	public void handle(EzyServerContext ctx, EzySession session, EzyArray data) {
		getLogger().debug("begin hanshake handler key = {} token = {}", data.get(1), data.get(2));
		updateSession(session, data);
		response(ctx, session);
		getLogger().debug("end hanshake handler, token = {}", session.getReconnectToken());
	}
	
	protected void updateSession(EzySession session, EzyArray data) {
		session.setClientId(data.get(0));
		session.setClientKey(EzyBase64.decode(data.get(1, String.class)));
	}
	
	protected boolean isReconnect(EzyServerContext ctx, String reconnectToken) {
		return getSessionManager(ctx).containsSession(reconnectToken);
	}
	
	protected void response(EzyContext ctx, EzySession session) {
		ctx.get(EzySendMessage.class)
			.data(getResponse(ctx, session))
			.sender(session).execute();
	}
	
	protected EzyArray getResponse(EzyContext ctx, EzySession session) {
		return serializeToArray(ctx, newResponse(session));
	}
	
	protected EzyHandShakeResponse newResponse(EzySession session) {
		return EzyHandShakeResponse.builder()
				.publicKey(encryptServerPublicKey(session))
				.reconnectToken(encryptReconnectToken(session))
				.build();
	}
	
	protected String encryptServerPublicKey(EzySession session) {
		return EzyBase64.decode2utf8(session.getPublicKey());
	}
	
	protected String encryptReconnectToken(EzySession session) {
		return encryptReconnectToken(session.getClientKey(), session.getReconnectToken());
	}
	
	protected String encryptReconnectToken(byte[] key, String token) {
		try {
			return tryEncryptReconnectToken(key, token);
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	protected String tryEncryptReconnectToken(byte[] key, String token) 
			throws Exception {
		return EzyAsyCrypt.builder()
				.algorithm("RSA")
				.publicKey(key)
				.build().encrypt(token, String.class);
	}

}
