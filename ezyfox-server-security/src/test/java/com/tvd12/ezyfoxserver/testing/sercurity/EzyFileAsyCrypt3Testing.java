package com.tvd12.ezyfoxserver.testing.sercurity;

import java.io.File;
import java.security.KeyPair;
import java.util.Arrays;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.file.EzyFileReader;
import com.tvd12.ezyfoxserver.file.EzyFileWriter;
import com.tvd12.ezyfoxserver.file.EzySimpleFileReader;
import com.tvd12.ezyfoxserver.file.EzySimpleFileWriter;
import com.tvd12.ezyfoxserver.io.EzyStrings;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.sercurity.EzyFileAsyCrypt;
import com.tvd12.ezyfoxserver.sercurity.EzyFileKeysGenerator;
import com.tvd12.test.base.BaseTest;
import static org.testng.Assert.*;

public class EzyFileAsyCrypt3Testing extends BaseTest {

	private EzyFileReader fileReader = EzySimpleFileReader.builder().build();
	private EzyFileWriter fileWriter = EzySimpleFileWriter.builder().build();
	
	private File outEncryptedFile 
			= new File("output/EzyFileAsyCrypt3Testing_outEncryptedFile.txt");
	private File outDecryptedFile 
			= new File("output/EzyFileAsyCrypt3Testing_outDecryptedFile.txt");
	
	@Test
	public void test() throws Exception {
		EzyFileKeysGenerator keysGenerator = EzyFileKeysGenerator.builder()
				.algorithm("RSA")
				.keysize(512)
				.publicKeyFile(new File("output/publickey.txt"))
				.privateKeyFile(new File("output/privatekey.txt"))
				.fileWriter(fileWriter)
				.build();
		keysGenerator.generate();
		KeyPair keyPair = keysGenerator.generate();
		
		System.err.println(Arrays.toString(keyPair.getPublic().getEncoded()));
		
		EzyFileAsyCrypt asyCrypt = EzyFileAsyCrypt.builder()
				.algorithm("RSA")
				.privateKeyFile(new File("output/privatekey.txt"))
				.publicKeyFile(new File("output/publickey.txt"))
				.fileReader(fileReader)
				.fileWriter(fileWriter)
				.outDecryptedFile(outDecryptedFile)
				.outEncryptedFile(outEncryptedFile)
				.build();
		
		assertEquals(keyPair.getPublic().getEncoded(), 
				fileReader.readBytes(new File("output/publickey.txt")));
		assertEquals(keyPair.getPrivate().getEncoded(), 
				fileReader.readBytes(new File("output/privatekey.txt")));
		
		String text = "i'm dzung";
		String encryptedText = asyCrypt.encrypt(text, String.class);
		
		System.out.println("encryptedText = " + encryptedText);
		
		String decryptedText = asyCrypt.decrypt(EzyBase64.decode(encryptedText), String.class);
		
		System.out.println("decryptedText = " + decryptedText);
		
		byte[] encrypt = asyCrypt.encrypt(EzyStrings.getUtfBytes("dungtv"));
		String decrypt = asyCrypt.decrypt(encrypt, String.class);
		assert decrypt.equals(EzyBase64.encodeUtf("dungtv"));
		
	}
	
}
