package com.tvd12.ezyfoxserver.constant;

import lombok.Getter;

public enum EzySessionRemoveReason implements EzyConstant {

	UNKNOWN(0),
	IDLE(1),
	NOT_LOGGED_IN(2);
	
	@Getter
	private final int id;
	
	private EzySessionRemoveReason(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzySessionRemoveReason valueOf(int id) {
		for(EzySessionRemoveReason reason : values())
			if(reason.getId() == id)
				return reason;
		throw new IllegalArgumentException("has no event with id = " + id);
	}
	
}
