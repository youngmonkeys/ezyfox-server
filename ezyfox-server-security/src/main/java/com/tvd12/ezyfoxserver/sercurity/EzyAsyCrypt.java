package com.tvd12.ezyfoxserver.sercurity;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;

import com.tvd12.ezyfoxserver.sercurity.function.BytesFunction;

public class EzyAsyCrypt {

	protected Cipher cipher;
	protected byte[] publicKey;
	protected byte[] privateKey;
	protected KeyFactory keyFactory;
	
	@SuppressWarnings("rawtypes")
	protected static final Map<Class, BytesFunction> BYTES_CONVERTERS;
	
	static {
		BYTES_CONVERTERS = defaultBytesConverters();
	}
	
	protected EzyAsyCrypt(Builder<?> builder) {
		this.cipher = builder.newCipher();
		this.publicKey = builder.publicKey;
		this.privateKey = builder.privateKey;
		this.keyFactory = builder.newKeyFactory();
	}
	
	public byte[] encrypt(byte[] data) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
		return cipher.doFinal(data);
    }
	
	public byte[] decrypt(byte[] data) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
		return cipher.doFinal(data);
    }
	
	public <T> T encrypt(byte[] data, Class<T> outType) throws Exception {
		byte[] bytes = encrypt(data);
		return convertBytes(bytes, outType);
	}
	
	public <T> T decrypt(byte[] data, Class<T> outType) throws Exception {
		byte[] bytes = decrypt(data);
		return convertBytes(bytes, outType);
	}
	
	public byte[] encrypt(String data) throws Exception {
		return encrypt(data.getBytes("UTF-8"));
    }
	
	public byte[] decrypt(String data) throws Exception {
		return decrypt(data.getBytes("UTF-8"));
    }
	
	public <T> T encrypt(String data, Class<T> outType) throws Exception {
		return encrypt(data.getBytes("UTF-8"), outType);
	}
	
	public <T> T decrypt(String data, Class<T> outType) throws Exception {
		return decrypt(data.getBytes("UTF-8"), outType);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T convertBytes(byte[] bytes, Class<T> outType) {
		return (T) getBytesConverters().get(outType).apply(bytes);
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, BytesFunction> getBytesConverters() {
		return BYTES_CONVERTERS;
	}

	/**
	 * @see https://docs.oracle.com/javase/8/docs/api/java/security/spec/PKCS8EncodedKeySpec.html
	 * 
	 * @return the private key object
	 * @throws Exception when get any errors
	 */
	protected PrivateKey getPrivateKey() throws Exception {
		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
	}

	/**
	 * @see https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html
	 * 
	 * @return the public key
	 * @throws Exception when get any error
	 */
	protected PublicKey getPublicKey() throws Exception {
		return keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));
	}
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, BytesFunction> defaultBytesConverters() {
		Map<Class, BytesFunction> answer = new ConcurrentHashMap<>();
		answer.put(byte[].class, (bytes) -> bytes);
		answer.put(String.class, (bytes) -> EzyBase64.encode2utf8((byte[])bytes));
		return answer;
	}
	
	public static Builder<?> builder() {
		return new Builder<>();
	}
	
	public static class Builder<B extends Builder<B>> {
		
		protected String algorithm;
		protected byte[] publicKey;
		protected byte[] privateKey;
		
		public B algorithm(String algorithm) {
			this.algorithm = algorithm;
			return getThis();
		}
		
		public B publicKey(byte[] publicKey) {
			this.publicKey = publicKey;
			return getThis();
		}
		
		public B privateKey(byte[] privateKey) {
			this.privateKey = privateKey;
			return getThis();
		}
		
		public EzyAsyCrypt build() {
			return new EzyAsyCrypt(this);
		}
		
		protected Cipher newCipher() {
			try {
				return Cipher.getInstance(algorithm);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		protected KeyFactory newKeyFactory() {
			try {
				return KeyFactory.getInstance(algorithm);
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		@SuppressWarnings("unchecked")
		protected B getThis() {
			return (B)this;
		}
	}

}
