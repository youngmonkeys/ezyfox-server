package com.tvd12.ezyfoxserver.testing;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RSATryTest {
	public static void main(String[] args) throws Exception {

		KeyPair keyPair = KeysGenerator.generate("RSA", 512);
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		
		byte[] input = new byte[] { (byte) 0xbe, (byte) 0xef };
		Cipher cipher = Cipher.getInstance("RSA");

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pubKeySpec = 
				new RSAPublicKeySpec(new BigInteger(new String(publicKey.getEncoded()), 16), new BigInteger("11", 16));
		RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger(new String(privateKey.getEncoded()), 16),
				new BigInteger("13", 16));

		RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
		RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);

		cipher.init(Cipher.ENCRYPT_MODE, pubKey);

		byte[] cipherText = cipher.doFinal(input);
		System.out.println("cipher: " + new String(cipherText));

		cipher.init(Cipher.DECRYPT_MODE, privKey);
		byte[] plainText = cipher.doFinal(cipherText);
		System.out.println("plain : " + new String(plainText));
	}
}

abstract class KeysGenerator {

	private KeysGenerator() {
	}
	
	public static KeyPair generate(String algorithm, int keyLength) {
		return generate(newKeyPairGenerator(algorithm), keyLength);
	}
	
	public static KeyPair generate(KeyPairGenerator generator, int keyLength) {
		generator.initialize(keyLength);
		return generator.generateKeyPair();
	}
	
	private static KeyPairGenerator newKeyPairGenerator(String algorithm) {
		try {
			return KeyPairGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
