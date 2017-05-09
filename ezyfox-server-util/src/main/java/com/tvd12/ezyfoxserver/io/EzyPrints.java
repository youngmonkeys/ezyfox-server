package com.tvd12.ezyfoxserver.io;

import java.util.Arrays;

public final class EzyPrints {

	private EzyPrints() {
	}
	
	public static String print(Object object) {
		if(object == null)
			return "null";
		if(object instanceof boolean[])
			return Arrays.toString((boolean[])object);
		if(object instanceof byte[])
			return Arrays.toString((byte[])object);
		if(object instanceof char[])
			return Arrays.toString((char[])object);
		if(object instanceof double[])
			return Arrays.toString((double[])object);
		if(object instanceof float[])
			return Arrays.toString((float[])object);
		if(object instanceof int[])
			return Arrays.toString((int[])object);
		if(object instanceof long[])
			return Arrays.toString((long[])object);
		if(object instanceof short[])
			return Arrays.toString((short[])object);
		if(object instanceof Object[])
			return Arrays.toString((Object[])object);
		return object.toString();
	}
	
	public static String printBits(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < bytes.length ; i++) 
			builder.append(printBits(bytes[i]));
		return builder.toString();
	}
	
	public static String printBits(byte value) {
		String str = insertBegin(Integer.toBinaryString(value & 0xff), "0", 8);
		return str.substring(str.length() - 8);
	}
	
	public static String insertBegin(String str, String ch, int maxlen) {
		String answer = str;
		while(answer.length() < maxlen)
			answer = ch + answer;
		return answer;
	}
	
}
