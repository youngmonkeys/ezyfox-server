package com.tvd12.ezyfoxserver.testing.sercurity;

import java.io.File;
import java.security.KeyPair;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.file.EzySimpleFileWriter;
import com.tvd12.ezyfoxserver.sercurity.EzyAsyCrypt;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.sercurity.EzyFileKeysGenerator;
import com.tvd12.test.base.BaseTest;

public class EzyAsyCryptTesting extends BaseTest {

	@Test
	public void test() throws Exception {
		EzyFileKeysGenerator keysGenerator = EzyFileKeysGenerator.builder()
				.algorithm("RSA")
				.keysize(512)
				.publicKeyFile(new File("output/publickey.txt"))
				.privateKeyFile(new File("output/privatekey.txt"))
				.fileWriter(EzySimpleFileWriter.builder().build())
				.build();
		KeyPair keyPair = keysGenerator.generate();
		
		EzyAsyCrypt asyCrypt = EzyAsyCrypt.builder()
				.algorithm("RSA")
				.privateKey(keyPair.getPrivate().getEncoded())
				.publicKey(keyPair.getPublic().getEncoded())
				.build();
		
		System.out.println("public: " + EzyBase64.encode2utf(keyPair.getPublic().getEncoded()));
		System.out.println("private: " + EzyBase64.encode2utf(keyPair.getPrivate().getEncoded()));
		
		String text = "i'm dzung";
		String encryptedText = asyCrypt.encrypt(text, String.class);
		
		System.out.println("encryptedText = " + encryptedText);
		
		String decryptedText = asyCrypt.decrypt(EzyBase64.decode(encryptedText), String.class);
		
		System.out.println("decryptedText = " + decryptedText);
		
	}
	
}
