package com.tvd12.ezyfoxserver.sercurity;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class EzyKeysGenerator {

	protected int keysize;
	protected String algorithm;
	
	protected EzyKeysGenerator(Builder<?> builder) {
		this.keysize = builder.keysize;
		this.algorithm = builder.algorithm;
	}

	public KeyPair generate() {
		return generate(newKeyPairGenerator());
	}
	
	protected KeyPair generate(KeyPairGenerator generator) {
		generator.initialize(keysize);
		KeyPair keyPair = generator.generateKeyPair();
		return keyPair;
	}
	
	protected KeyPairGenerator newKeyPairGenerator() {
		try {
			return KeyPairGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Builder builder() {
		return new Builder<>();
	}

	@SuppressWarnings("unchecked")
	public static class Builder<B extends Builder<B>> {
		protected int keysize = 512;
		protected String algorithm = "RSA";
		
		public B keysize(int keysize) {
			this.keysize = keysize;
			return (B)this;
		}
		public B algorithm(String algorithm) {
			this.algorithm = algorithm;
			return (B)this;
		}
		
		public EzyKeysGenerator build() {
			return new EzyKeysGenerator(this);
		}
	}
}
