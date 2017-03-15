package com.tvd12.ezyfoxserver.constant;

import lombok.Getter;

public enum EzyEventType implements EzyConstant {

	SERVER_READY(1),
	USER_REQUEST(2);
	
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
		for(EzyEventType event : values())
			if(event.getId() == id)
				return event;
		throw new IllegalArgumentException("has no event with id = " + id);
	}
	
}
