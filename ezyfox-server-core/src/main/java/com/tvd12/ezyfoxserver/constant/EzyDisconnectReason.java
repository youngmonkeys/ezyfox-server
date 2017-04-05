package com.tvd12.ezyfoxserver.constant;

import lombok.Getter;

public enum EzyDisconnectReason implements EzyConstant {

	UNKNOWN(0),
	IDLE(1),
	NOT_LOGGED_IN(2),
	ANOTHER_DEVICE_LOGIN(3);
	
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
		for(EzyDisconnectReason reason : values())
			if(reason.getId() == id)
				return reason;
		throw new IllegalArgumentException("has no event with id = " + id);
	}
	
}
