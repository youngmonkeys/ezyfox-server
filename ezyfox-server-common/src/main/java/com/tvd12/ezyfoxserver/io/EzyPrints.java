package com.tvd12.ezyfoxserver.io;

public abstract class EzyPrints {

	private EzyPrints() {
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
