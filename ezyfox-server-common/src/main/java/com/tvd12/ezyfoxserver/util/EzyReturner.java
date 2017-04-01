package com.tvd12.ezyfoxserver.util;

public abstract class EzyReturner {

	private EzyReturner() {
	}
	
	public static <T> T returnNotNull(T rvalue, T svalue) {
		return rvalue != null ? rvalue : svalue;
	}
	
}
