package com.tvd12.ezyfoxserver.sercurity;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;

import com.tvd12.ezyfoxserver.function.EzyBytesFunction;

public class EzyAsyCrypt {

	protected Cipher cipher;
	protected byte[] publicKey;
	protected byte[] privateKey;
	protected KeyFactory keyFactory;
	
	@SuppressWarnings("rawtypes")
	protected static final Map<Class, EzyBytesFunction> BYTES_CONVERTERS = defaultBytesConverters();
	
	protected EzyAsyCrypt(Builder<?> builder) {
		try {
			init(builder);
		}
		catch(Exception e) {
			throw new IllegalStateException("init asymmetric encryption error", e);
		}
	}
	
	protected void init(Builder<?> builder) throws Exception {
		this.cipher = builder.newCipher();
		this.publicKey = builder.getPublicKey();
		this.privateKey = builder.getPrivateKey();
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
		return decrypt(EzyBase64.decode(data));
    }
	
	public <T> T encrypt(String data, Class<T> outType) throws Exception {
		return encrypt(data.getBytes("UTF-8"), outType);
	}
	
	public <T> T decrypt(String data, Class<T> outType) throws Exception {
		return decrypt(EzyBase64.decode(data), outType);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T convertBytes(byte[] bytes, Class<T> outType) {
		return (T) getBytesConverters().get(outType).apply(bytes);
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, EzyBytesFunction> getBytesConverters() {
		return BYTES_CONVERTERS;
	}

	/**
	 * https://docs.oracle.com/javase/8/docs/api/java/security/spec/PKCS8EncodedKeySpec.html
	 * 
	 * @return the private key object
	 * @throws Exception when get any errors
	 */
	protected PrivateKey getPrivateKey() throws Exception {
		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
	}

	/**
	 * https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html
	 * 
	 * @return the public key
	 * @throws Exception when get any error
	 */
	protected PublicKey getPublicKey() throws Exception {
		return keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));
	}
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, EzyBytesFunction> defaultBytesConverters() {
		Map<Class, EzyBytesFunction> answer = new ConcurrentHashMap<>();
		answer.put(byte[].class, (bytes) -> bytes);
		answer.put(String.class, (bytes) -> EzyBase64.encode2utf((byte[])bytes));
		return answer;
	}
	
	public static Builder<?> builder() {
		return new Builder<>();
	}
	
	@SuppressWarnings("unchecked")
	public static class Builder<B extends Builder<B>> {
		
		protected String algorithm;
		protected byte[] publicKey;
		protected byte[] privateKey;
		
		public B algorithm(String algorithm) {
			this.algorithm = algorithm;
			return (B)this;
		}
		
		public B publicKey(byte[] publicKey) {
			this.publicKey = publicKey;
			return (B)this;
		}
		
		public B privateKey(byte[] privateKey) {
			this.privateKey = privateKey;
			return (B)this;
		}
		
		protected byte[] getPublicKey() {
			return publicKey;
		}
		
		protected byte[] getPrivateKey() {
			return privateKey;
		}
		
		public EzyAsyCrypt build() {
			return new EzyAsyCrypt(this);
		}
		
		protected Cipher newCipher() throws Exception {
			return Cipher.getInstance(algorithm);
		}
		
		protected KeyFactory newKeyFactory() throws Exception {
			return KeyFactory.getInstance(algorithm);
		}
	}

}
