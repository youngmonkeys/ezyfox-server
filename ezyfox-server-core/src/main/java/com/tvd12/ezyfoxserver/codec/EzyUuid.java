package com.tvd12.ezyfoxserver.codec;

import java.util.UUID;

public abstract class EzyUuid {

	private EzyUuid() {
	}
	
	public static UUID random() {
		return UUID.randomUUID();
	}
	
}
