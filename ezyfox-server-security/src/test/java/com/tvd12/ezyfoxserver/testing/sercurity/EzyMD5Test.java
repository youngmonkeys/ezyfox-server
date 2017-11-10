package com.tvd12.ezyfoxserver.testing.sercurity;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.commons.codec.digest.Md5Crypt;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyStrings;
import com.tvd12.ezyfoxserver.sercurity.EzyMD5;
import com.tvd12.test.base.BaseTest;

public class EzyMD5Test extends BaseTest {

	static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	@Override
	public Class<?> getTestClass() {
		return EzyMD5.class;
	}

	@Test
	public void test() throws UnsupportedEncodingException {
		String salt = "$1$" + B64.getRandomSalt(8);
		System.out.println(salt);
		System.out.println("a: " + EzyMD5.cryptUtf("dungtv"));
		System.out.println("b: " + Md5Crypt.md5Crypt(EzyStrings.getUtfBytes("dungtv")));
		assert EzyMD5.cryptUtf("dungtv", salt).equals(Md5Crypt.md5Crypt(EzyStrings.getUtfBytes("dungtv"), salt));
	}

	public static class B64 {
		static String getRandomSalt(int num) {
			StringBuilder saltString = new StringBuilder();
	        for (int i = 1; i <= num; i++) 
	            saltString.append(B64T.charAt(new Random().nextInt(B64T.length())));
	       return saltString.toString();
	    }
	}
}
