package com.tvd12.ezyfoxserver.io;

public abstract class EzyMath {

	private EzyMath() {
	}
	
	public static int bin2int(int length) {
		return (int) bin2long(length);
	}
	
	public static int bin2int(byte[] bytes) {
		return (int) bin2ulong(bytes);
	}
	
	public static long bin2long(int length) {
		int result = 0;
		for(int i = 0 ; i < length ; i++)
			result |= 1 << i;
		return result;
	}
	
	public static long bin2ulong(byte[] bytes) {
		long result = 0;
		int size = bytes.length - 1;
		for(int i = 0 ; i < bytes.length ; i++)
			result += ((bytes[i] & 0xff) << ((size - i) * 8));
		return result;
	}
	
	public static long bin2long(byte[] bytes) {
		return -(((bin2ulong(bytes)) - 1) ^ ((1 << bytes.length * 8) - 1));
	}
	
	public static void main(String[] args) {
		int value = -EzyMath.bin2int(14);
		
		byte[] bytes = EzyBytes.getBytes(value, 3);
		System.out.println(EzyPrints.printBits(bytes));
		System.out.println("value = " + value  + ", " + bin2long(bytes) + " " + (3 << 1));
	}
	
}
