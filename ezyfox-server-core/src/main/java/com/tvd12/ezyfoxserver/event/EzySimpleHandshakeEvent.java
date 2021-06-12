package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzySimpleHandshakeEvent
		extends EzySimpleSessionEvent 
		implements EzyHandshakeEvent {

	protected final String clientId;
	protected final byte[] clientKey;
	protected final String clientType;
	protected final String clientVersion;
	protected final String reconnectToken;
	protected final boolean enableEncryption;
	@Setter
	protected byte[] sessionKey;
	@Setter
	protected byte[] encryptedSessionKey;
	
	public EzySimpleHandshakeEvent(
			EzySession session,
			String clientId,
			byte[] clientKey,
			String clientType,
			String clientVersion,
			String reconnectToken,
			boolean enableEncryption
	) {
		super(session);
		this.clientId = clientId;
		this.clientKey = clientKey;
		this.clientType = clientType;
		this.clientVersion = clientVersion;
		this.reconnectToken = reconnectToken;
		this.enableEncryption = enableEncryption;
	}
	
	@Override
	public void release() {
		this.sessionKey = null;
		this.encryptedSessionKey = null;
	}
}
