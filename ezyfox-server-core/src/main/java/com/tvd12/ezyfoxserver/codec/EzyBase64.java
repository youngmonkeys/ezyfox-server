package com.tvd12.ezyfoxserver.codec;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public abstract class EzyBase64 {

	private EzyBase64() {
	}
	
	public static String encodeUTF(final String input) {
		try {
			return Base64.encodeBase64String(input.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String decodeUTF(final String input) {
		try {
			return new String(Base64.decodeBase64(input), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static byte[] encode(final byte[] input) {
		return Base64.encodeBase64(input);
	}
	
	public static byte[] decode(final byte[] input) {
		return Base64.decodeBase64(input);
	}
	
}
