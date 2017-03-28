package com.tvd12.ezyfoxserver.sercurity;

import java.io.File;

import com.tvd12.ezyfoxserver.io.EzyFileReader;
import com.tvd12.ezyfoxserver.io.EzyFileWriter;

public class EzyFileAsyCrypt extends EzyAsyCrypt {
	
	protected File outEncryptedFile;
	protected File outDecryptedFile;
	protected EzyFileWriter fileWriter;
	protected EzyFileReader fileReader;
	
	protected EzyFileAsyCrypt(Builder builder) {
		super(builder);
		this.fileReader = builder.fileReader;
		this.fileWriter = builder.fileWriter;
		this.publicKey = builder.getPublicKey();
		this.privateKey = builder.getPrivateKey();
		this.outDecryptedFile = builder.outDecryptedFile;
		this.outEncryptedFile = builder.outEncryptedFile;
	}
	
	@Override
	public byte[] encrypt(byte[] data) throws Exception {
		byte[] bytes = super.encrypt(data);
		writeToFile(bytes, outEncryptedFile);
		return bytes;
	}
	
	@Override
	public byte[] decrypt(byte[] data) throws Exception {
		byte[] bytes = super.decrypt(data);
		writeToFile(data, outDecryptedFile);
		return bytes;
	}
	
	public byte[] encrypt(File file) throws Exception {
		return encrypt(readFile(file));
	}
	
	public byte[] decrypt(File file) throws Exception {
		return decrypt(readFile(file));
	}
	
	public <T> T encrypt(File file, Class<T> outType) throws Exception {
		byte[] bytes = encrypt(file);
		return convertBytes(bytes, outType);
	}
	
	public <T> T decrypt(File file, Class<T> outType) throws Exception {
		byte[] bytes = decrypt(file);
		return convertBytes(bytes, outType);
	}
	
	protected byte[] readFile(File file) {
		return fileReader.readBytes(file);
	}
	
	protected void writeToFile(byte[] data, File file) {
		if(file != null)
			fileWriter.write(data, file);
	}

	public static class Builder extends EzyAsyCrypt.Builder<Builder> {
		protected File publicKeyFile;
		protected File privateKeyFile;
		protected File outEncryptedFile;
		protected File outDecryptedFile;
		protected EzyFileWriter fileWriter;
		protected EzyFileReader fileReader;
		
		public Builder publicKeyFile(File publicKeyFile) {
			this.publicKeyFile = privateKeyFile;
			return this;
		}
		
		public Builder privateKeyFile(File privateKeyFile) {
			this.privateKeyFile = privateKeyFile;
			return this;
		}
		public Builder outEncryptedFile(File outEncryptedFile) {
			this.outEncryptedFile = outEncryptedFile;
			return this;
		}
		
		public Builder outDecryptedFile(File outDecryptedFile) {
			this.outDecryptedFile = outDecryptedFile;
			return this;
		}
		
		public Builder fileWriter(EzyFileWriter fileWriter) {
			this.fileWriter = fileWriter;
			return this;
		}
		
		public Builder fileReader(EzyFileReader fileReader) {
			this.fileReader = fileReader;
			return this;
		}
		
		@Override
		public EzyAsyCrypt build() {
			return new EzyFileAsyCrypt(this);
		}
		
		protected byte[] getPublicKey() {
			return publicKey != null ? publicKey : readPublicKeyFile();
		}
		
		protected byte[] getPrivateKey() {
			return privateKey != null ? privateKey : readPrivateKeyFile();
		}
		
		protected byte[] readPublicKeyFile() {
			return fileReader.readBytes(publicKeyFile);
		}
		
		protected byte[] readPrivateKeyFile() {
			return fileReader.readBytes(privateKeyFile);
		}
	}
	
}
