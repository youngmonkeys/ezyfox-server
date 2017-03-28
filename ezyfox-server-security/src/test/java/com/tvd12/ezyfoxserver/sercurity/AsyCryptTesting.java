package com.tvd12.ezyfoxserver.sercurity;

import java.security.KeyPair;

import org.testng.annotations.Test;

import com.tvd12.test.base.BaseTest;

public class AsyCryptTesting extends BaseTest {

	@Test
	public void test() throws Exception {
		EzyKeysGenerator keysGenerator = EzyKeysGenerator.builder()
				.algorithm("RSA")
				.keyLength(512)
				.build();
		KeyPair keyPair = keysGenerator.generate();
		
		EzyAsyCrypt asyCrypt = EzyAsyCrypt.builder()
				.algorithm("RSA")
				.privateKey(keyPair.getPrivate().getEncoded())
				.publicKey(keyPair.getPublic().getEncoded())
				.build();
		
		String text = "i'm dzung";
		String encryptedText = asyCrypt.encrypt(text, String.class);
		
		System.out.println("encryptedText = " + encryptedText);
		
		String decryptedText = asyCrypt.decrypt(EzyBase64.decode(encryptedText), String.class);
		
		System.out.println("decryptedText = " + decryptedText);
		
	}
	
}
