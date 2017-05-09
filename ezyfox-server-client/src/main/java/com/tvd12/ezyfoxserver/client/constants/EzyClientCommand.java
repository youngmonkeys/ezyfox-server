package com.tvd12.ezyfoxserver.client.constants;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.util.EzyEnums;

import lombok.Getter;

public enum EzyClientCommand implements EzyConstant {

	ACESS_APP_SUCCESS(0),
	CONNECT_SUCCESS(1),
	CONNECT_FAILURE(2),
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
	APP_EXIT(33);
	
	@Getter
	private final int id;
	
	private EzyClientCommand(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzyClientCommand valueOf(int id) {
		return EzyEnums.valueOf(values(), id);
	}
	
}
