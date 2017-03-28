package com.tvd12.ezyfoxserver.sercurity;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class EzyKeysGenerator {

	protected int keyLength;
	protected String algorithm;
	
	protected EzyKeysGenerator(Builder<?> builder) {
		this.keyLength = builder.keyLength;
		this.algorithm = builder.algorithm;
	}

	public KeyPair generate() {
		return generate(newKeyPairGenerator());
	}
	
	protected KeyPair generate(KeyPairGenerator generator) {
		generator.initialize(keyLength);
		return generator.generateKeyPair();
	}
	
	protected KeyPairGenerator newKeyPairGenerator() {
		try {
			return KeyPairGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static Builder<?> builder() {
		return new Builder<>();
	}
	
	public static class Builder<B extends Builder<B>> {
		protected int keyLength;
		protected String algorithm;
		
		public B keyLength(int keyLength) {
			this.keyLength = keyLength;
			return getThis();
		}
		public B algorithm(String algorithm) {
			this.algorithm = algorithm;
			return getThis();
		}
		
		public EzyKeysGenerator build() {
			return new EzyKeysGenerator(this);
		}
		
		@SuppressWarnings("unchecked")
		protected B getThis() {
			return (B)this;
		}
	}
}
