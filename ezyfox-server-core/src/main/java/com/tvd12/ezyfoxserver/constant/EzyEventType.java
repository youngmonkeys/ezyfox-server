package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfoxserver.util.EzyEnums;

import lombok.Getter;

public enum EzyEventType implements EzyConstant {

	SERVER_READY(1),
	USER_HANDSHAKE(20),
	USER_LOGIN(21),
	USER_SESSION_LOGIN(22),
	USER_REQUEST(23),
	USER_DISCONNECT(24),
    USER_RECONNECT(25),
    USER_ACCESS_APP(26),
    USER_JOINED_APP(27),
    USER_ADDED(28),
    USER_REMOVED(29),
    SESSION_REMOVED(35);
    
	@Getter
	private final int id;
	
	private EzyEventType(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzyEventType valueOf(int id) {
		return EzyEnums.valueOf(values(), id);
	}
	
}
