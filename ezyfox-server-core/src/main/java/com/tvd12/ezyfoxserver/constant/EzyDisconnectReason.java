package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyEnums;

import lombok.Getter;

public enum EzyDisconnectReason implements EzyConstant {

	UNKNOWN(0),
	IDLE(1),
	NOT_LOGGED_IN(2),
	ANOTHER_SESSION_LOGIN(3),
	ADMIN_BAN(4),
    ADMIN_KICK(5),
    MAX_REQUEST_PER_SECOND(6),
    MAX_REQUEST_SIZE(7),
    SERVER_ERROR(8);
	
	@Getter
	private final int id;
	
	private EzyDisconnectReason(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzyDisconnectReason valueOf(int id) {
		return EzyEnums.valueOf(values(), id);
	}
	
}
