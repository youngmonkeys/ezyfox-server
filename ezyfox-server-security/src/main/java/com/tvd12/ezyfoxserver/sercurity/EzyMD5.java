package com.tvd12.ezyfoxserver.sercurity;

import org.apache.commons.codec.digest.Md5Crypt;

import com.tvd12.ezyfoxserver.io.EzyStrings;

public final class EzyMD5 {

	private EzyMD5() {
	}
	
	public static String cryptUtf(String input) {
		return cryptUtf(input, "$1$ezyfox-server");
	}
	
	public static String cryptUtf(String input, String salt) {
		return Md5Crypt.md5Crypt(EzyStrings.getUtfBytes(input), salt);
	}
	
}
