package com.tvd12.ezyfoxserver.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public abstract class EzyStringUtil {

	private EzyStringUtil() {
	}
	
	public static String newUTF(byte[] bytes) {
		return newString(bytes, "UTF-8");
	}
	
	public static String newUTF(ByteBuffer buffer, int size) {
		return newString(buffer, size, "UTF-8");
	}
	
	public static String newString(byte[] bytes, String charset) {
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String newString(ByteBuffer buffer, int size, String charset) {
		byte[] bytes = new byte[size];
		buffer.get(bytes);
		return newString(bytes, charset);
	}
}
