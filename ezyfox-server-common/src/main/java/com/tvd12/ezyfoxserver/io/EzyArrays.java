package com.tvd12.ezyfoxserver.io;

public abstract class EzyArrays {

	private EzyArrays() {
	}
	
	public static void copy(byte[] from, byte[] to, int toPos) {
		for(int i = 0 ; i < from.length ; i++)
			to[toPos + i] = from[i];
	}
	
	public static byte[] merge(byte first, byte[] other) {
		byte[] bytes = new byte[other.length + 1];
		bytes[0] = first;
		for(int i = 0 ; i < other.length ; i++)
			bytes[i + 1] = other[i];
		return bytes;
	}
	
}
