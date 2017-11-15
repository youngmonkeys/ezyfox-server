package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfoxserver.util.EzyEnums;

import lombok.Getter;

public enum EzyCommand implements EzyConstant {

	ERROR(10),
	HANDSHAKE(11),
	PING(12),
    PONG(13),
    DISCONNECT(14),
    PLUGIN_REQUEST(15),
	LOGIN(20),
	LOGIN_ERROR(21),
	LOGOUT(22),
	APP_ACCESS(30),
	APP_REQUEST(31),
	APP_JOINED(32),
	APP_EXIT(33),
	APP_ACCESS_ERROR(34);
	
	@Getter
	private final int id;
	
	private EzyCommand(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzyCommand valueOf(int id) {
		return EzyEnums.valueOf(values(), id);
	}
	
}
