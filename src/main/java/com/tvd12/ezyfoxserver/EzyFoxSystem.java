package com.tvd12.ezyfoxserver;

public class EzyFoxSystem {

	private static EzyFoxEnvironment env;
	
	static {
		env = new EzyFoxEnvironment();
	}
	
	private EzyFoxSystem() {
	}
	
	public static EzyFoxEnvironment getEnv() {
		return env;
	}
	
}
