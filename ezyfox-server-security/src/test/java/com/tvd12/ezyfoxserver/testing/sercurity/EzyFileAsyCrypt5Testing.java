package com.tvd12.ezyfoxserver.testing.sercurity;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.file.EzyFileReader;
import com.tvd12.ezyfoxserver.file.EzyFileWriter;
import com.tvd12.ezyfoxserver.file.EzySimpleFileReader;
import com.tvd12.ezyfoxserver.file.EzySimpleFileWriter;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.sercurity.EzyFileAsyCrypt;
import com.tvd12.ezyfoxserver.sercurity.EzyFileKeysGenerator;
import com.tvd12.test.base.BaseTest;

public class EzyFileAsyCrypt5Testing extends BaseTest {

	private EzyFileReader fileReader = EzySimpleFileReader.builder().build();
	private EzyFileWriter fileWriter = EzySimpleFileWriter.builder().build();

	private File fileInput 
			= new File("output/EzyFileAsyCrypt3Testing_fileInput.txt");
	
	private File outEncryptedFile 
			= new File("output/EzyFileAsyCrypt3Testing_outEncryptedFile.txt");
	private File outDecryptedFile 
			= new File("output/EzyFileAsyCrypt3Testing_outDecryptedFile.txt");
	
	public EzyFileAsyCrypt5Testing() throws IOException {
		super();
		FileUtils.forceMkdirParent(fileInput);
		fileWriter.write(fileInput, new String("dungtv").getBytes("UTF-8"));
	}
	
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
		
		byte[] encrypt = asyCrypt.encrypt(fileInput, byte[].class);
		assertEquals(encrypt, fileReader.readBytes(outEncryptedFile));
		byte[] decrypt = asyCrypt.decrypt(outEncryptedFile, byte[].class);
		assertEquals(decrypt, fileReader.readBytes(outDecryptedFile));
		String string = asyCrypt.decrypt(outEncryptedFile, String.class);
		assert string.equals(EzyBase64.encodeUtf("dungtv"));
	}
	
}
