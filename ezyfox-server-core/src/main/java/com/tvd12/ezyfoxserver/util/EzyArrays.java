package com.tvd12.ezyfoxserver.util;

public abstract class EzyArrays {

	private EzyArrays() {
	}
	
	public static void copy(byte[] from, byte[] to, int toPos) {
		for(int i = 0 ; i < from.length ; i++)
			to[toPos + i] = from[i];
	}
	
}
