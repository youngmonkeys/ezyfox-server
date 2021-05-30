package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfox.sercurity.EzyAesCrypt;
import com.tvd12.ezyfox.sercurity.EzyAsyCrypt;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyHandshakeEvent;

public class EzyRsaAesHandshakeController
		extends EzyLoggable
		implements EzyEventController<EzyServerContext, EzyHandshakeEvent> {

	@Override
	public void handle(EzyServerContext ctx, EzyHandshakeEvent event) {
		EzySession session = event.getSession();
		if(session.getConnectionType() == EzyConnectionType.WEBSOCKET)
			return;
		if(!event.isEnableEncryption())
			return;
		byte[] clientKey = event.getClientKey();
		byte[] sessionKey = EzyAesCrypt.randomKey();
		byte[] encryptedSessionKey = sessionKey;
		try {
			if(clientKey.length > 0) {
				encryptedSessionKey = EzyAsyCrypt.builder()
						.publicKey(clientKey)
						.build()
						.encrypt(sessionKey);
			}
		}
		catch (Exception e) {
			logger.debug("cannot encrypte session key for session: {}", session, e);
		}
		event.setSessionKey(sessionKey);
		event.setEncryptedSessionKey(encryptedSessionKey);
	}
	
}
