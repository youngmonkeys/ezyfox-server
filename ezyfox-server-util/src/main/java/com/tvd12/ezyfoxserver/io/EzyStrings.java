package com.tvd12.ezyfoxserver.io;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public final class EzyStrings {

	private EzyStrings() {
	}
	
	public static String newUtf(byte[] bytes) {
		return newString(bytes, "UTF-8");
	}
	
	public static String newUtf(ByteBuffer buffer, int size) {
		return newString(buffer, size, "UTF-8");
	}
	
	public static byte[] getUtfBytes(String str) {
		return getBytes(str, "UTF-8");
	}
	
	public static String newString(byte[] bytes, String charset) {
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static byte[] getBytes(String str, String charset) {
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String newString(ByteBuffer buffer, int size, String charset) {
		byte[] bytes = new byte[size];
		buffer.get(bytes);
		return newString(bytes, charset);
	}
	
	public static String getString(String[] array, int index, String def) {
		return array.length > index ? array[index] : def;
	}
}
