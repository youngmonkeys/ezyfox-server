package com.tvd12.ezyfoxserver.codec;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.Md5Crypt;

public abstract class EzyMD5 {

	private EzyMD5() {
	}
	
	public static String cryptUTF(final String input) {
		try {
			return Md5Crypt.md5Crypt(input.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
