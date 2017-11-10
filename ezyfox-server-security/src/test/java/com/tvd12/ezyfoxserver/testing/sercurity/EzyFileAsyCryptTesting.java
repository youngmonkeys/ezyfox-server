package com.tvd12.ezyfoxserver.testing.sercurity;

import java.io.File;
import java.security.KeyPair;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.file.EzyFileReader;
import com.tvd12.ezyfoxserver.file.EzyFileWriter;
import com.tvd12.ezyfoxserver.file.EzySimpleFileReader;
import com.tvd12.ezyfoxserver.file.EzySimpleFileWriter;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.sercurity.EzyFileAsyCrypt;
import com.tvd12.ezyfoxserver.sercurity.EzyFileKeysGenerator;
import com.tvd12.test.base.BaseTest;

public class EzyFileAsyCryptTesting extends BaseTest {

	private EzyFileReader fileReader = EzySimpleFileReader.builder().build();
	private EzyFileWriter fileWriter = EzySimpleFileWriter.builder().build();
	
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
		
		EzyFileAsyCrypt asyCrypt = EzyFileAsyCrypt.builder()
				.algorithm("RSA")
				.privateKey(keyPair.getPrivate().getEncoded())
				.publicKey(keyPair.getPublic().getEncoded())
				.fileReader(fileReader)
				.fileWriter(fileWriter)
				.build();
		
		String text = "i'm dzung";
		String encryptedText = asyCrypt.encrypt(text, String.class);
		
		System.out.println("encryptedText = " + encryptedText);
		
		String decryptedText = asyCrypt.decrypt(EzyBase64.decode(encryptedText), String.class);
		
		System.out.println("decryptedText = " + decryptedText);
		
	}
	
}
