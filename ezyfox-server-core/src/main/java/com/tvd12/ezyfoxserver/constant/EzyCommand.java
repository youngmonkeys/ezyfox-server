package com.tvd12.ezyfoxserver.constant;

import lombok.Getter;

public enum EzyCommand implements EzyConstant {

	ERROR(0),
	HAND_SHAKE(1),
	LOGIN(2);
	
	@Getter
	private final int id;
	
	private EzyCommand(final int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static EzyCommand valueOf(final int id) {
		for(EzyCommand cmd : values())
			if(cmd.getId() == id)
				return cmd;
		throw new IllegalArgumentException("has no command with id = " + id);
	}
	
}
