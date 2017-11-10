package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfoxserver.util.EzyEnums;

import lombok.Getter;

public enum EzyUserRemoveReason implements EzyConstant {

	UNKNOWN(0),
	IDLE(1),
	ADMIN_BAN(4),
	ADMIN_KICK(5);
	
	@Getter
	private final int id;
	
	private EzyUserRemoveReason(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzyUserRemoveReason valueOf(int id) {
		return EzyEnums.valueOf(values(), id);
	}
	
}
