package com.tvd12.ezyfoxserver.sercurity;

import java.io.File;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.tvd12.ezyfoxserver.file.EzyFileWriter;

public class EzyFileKeysGenerator extends EzyKeysGenerator {

	protected File publicKeyFile;
	protected File privateKeyFile;
	protected EzyFileWriter fileWriter;
	
	protected EzyFileKeysGenerator(Builder builder) {
		super(builder);
		this.fileWriter = builder.fileWriter;
		this.publicKeyFile = builder.publicKeyFile;
		this.privateKeyFile = builder.privateKeyFile;
	}
	
	@Override
	public KeyPair generate() {
		KeyPair keyPair = super.generate();
		writePublicKey(keyPair.getPublic());
		writePrivateKey(keyPair.getPrivate());
		return keyPair;
	}
	
	protected void writePublicKey(PublicKey key) {
		writeKey(publicKeyFile, key);
	}
	
	protected void writePrivateKey(PrivateKey key) {
		writeKey(privateKeyFile, key);
	}
	
	protected void writeKey(File file, Key key) {
		if(file != null)
			fileWriter.write(file, key.getEncoded());
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzyKeysGenerator.Builder<Builder> {
		protected File publicKeyFile;
		protected File privateKeyFile;
		protected EzyFileWriter fileWriter;
		
		public Builder fileWriter(EzyFileWriter fileWriter) {
			this.fileWriter = fileWriter;
			return this;
		}
		
		public Builder publicKeyFile(File file) {
			this.publicKeyFile = file;
			return this;
		}
		
		public Builder privateKeyFile(File file) {
			this.privateKeyFile = file;
			return this;
		}
		
		@Override
		public EzyFileKeysGenerator build() {
			return new EzyFileKeysGenerator(this);
		}
		
	}
	
}
