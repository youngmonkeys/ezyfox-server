package com.tvd12.ezyfoxserver.sercurity;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public abstract class EzyBase64 {

	private EzyBase64() {
	}
	
	public static byte[] encode(byte[] input) {
		return Base64.encodeBase64(input);
	}
	
	public static byte[] decode(byte[] input) {
		return Base64.decodeBase64(input);
	}
	
	public static byte[] decode(String input) {
		return Base64.decodeBase64(input);
	}
	
	public static byte[] encode(String input) {
		try {
			return Base64.encodeBase64(input.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String encodeUTF(String input) {
		try {
			return Base64.encodeBase64String(input.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String decodeUTF(String input) {
		try {
			return new String(Base64.decodeBase64(input), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String encode2utf8(byte[] input) {
		try {
			return new String(encode(input), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String decode2utf8(byte[] input) {
		try {
			return new String(decode(input), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
