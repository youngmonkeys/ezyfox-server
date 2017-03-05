package com.tvd12.ezyfoxserver.util;

public abstract class EzyMath {

	private EzyMath() {
	}
	
	public static int bin2int(int length) {
		int result = 0;
		for(int i = 0 ; i < length ; i++)
			result |= 1 << i;
		return result;
	}
	
	
	public static void main(String[] args) {
		System.out.println(bin2int(16));
	}
}
