package com.tvd12.ezyfoxserver.client.request;

import java.io.File;
import java.security.KeyPair;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.io.EzySimpleFileReader;
import com.tvd12.ezyfoxserver.io.EzySimpleFileWriter;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.sercurity.EzyFileAsyCrypt;
import com.tvd12.ezyfoxserver.sercurity.EzyFileKeysGenerator;

public class EzyHandShakeRequest extends EzyBaseRequest implements EzyRequest {

	private KeyPair keyPair;
	private String publicKey;
	private String reconnectToken;
	
	protected EzyHandShakeRequest(Builder builder) {
		this.reconnectToken = builder.fetchReconnectToken();
		this.keyPair = builder.newKeyPair();
		this.publicKey = EzyBase64.encode2utf8(keyPair.getPublic().getEncoded());
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.HAND_SHAKE;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(publicKey)
				.append(reconnectToken)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		public EzyHandShakeRequest build() {
			return new EzyHandShakeRequest(this);
		}
		
		protected String fetchReconnectToken() {
			return new ReconnectTokenFetcher().get();
		}
		
		protected KeyPair newKeyPair() {
			return EzyFileKeysGenerator.builder()
					.keyLength(512)
					.algorithm("RSA")
					.fileWriter(new EzySimpleFileWriter())
					.privateKeyFile(getPrivateKeyFile())
					.build()
					.generate();
		}
		
		private File getPrivateKeyFile() {
			return new File("output/private_key.txt");
		}
	}

}

class ReconnectTokenFetcher {
	
	public String get() {
		return decryptToken();
	}
	
	private String decryptToken() {
		try {
			return tryDecryptToken();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	private String tryDecryptToken() throws Exception {
		return EzyFileAsyCrypt.builder()
				.algorithm("RSA")
				.privateKeyFile(getPrivateKeyFile())
				.fileReader(new EzySimpleFileReader())
				.build()
				.decrypt(getTokenFile(), String.class);
	}
	
	private File getTokenFile() {
		return new File("output/reconnect_token.txt");
	}
	
	private File getPrivateKeyFile() {
		return new File("output/private_key.txt");
	}
	
}
