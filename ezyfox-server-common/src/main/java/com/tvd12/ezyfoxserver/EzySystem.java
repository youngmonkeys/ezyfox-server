package com.tvd12.ezyfoxserver;

public class EzySystem {

	private static EzyEnvironment env;
	
	static {
		env = new EzyEnvironment();
	}
	
	private EzySystem() {
	}
	
	public static EzyEnvironment getEnv() {
		return env;
	}
	
}
