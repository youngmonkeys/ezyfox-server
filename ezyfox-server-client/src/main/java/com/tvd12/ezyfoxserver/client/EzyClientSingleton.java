package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.Getter;
import lombok.Setter;

public class EzyClientSingleton {

	@Setter
	@Getter
	private EzyUser me;
	
	private static final EzyClientSingleton INSTANCE;
	
	static {
		INSTANCE = new EzyClientSingleton();
	}
	
	private EzyClientSingleton() {
	}
	
	public static EzyClientSingleton getInstance() {
		return INSTANCE;
	}
	
}
