package com.tvd12.ezyfoxserver.client.request;

import java.io.File;
import java.security.KeyPair;

import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.file.EzySimpleFileReader;
import com.tvd12.ezyfoxserver.file.EzySimpleFileWriter;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.sercurity.EzyFileAsyCrypt;
import com.tvd12.ezyfoxserver.sercurity.EzyFileKeysGenerator;

public class EzyHandShakeRequest extends EzyBaseRequest implements EzyRequest {

	private KeyPair keyPair;
	private String publicKey;
	private String reconnectToken;
	private String clientId = "";
	private String clientType = "JAVA";
	private String clientVersion = "1.0.0";
	
	protected EzyHandShakeRequest(Builder builder) {
		this.reconnectToken = builder.fetchReconnectToken();
		this.keyPair = builder.newKeyPair();
		this.publicKey = EzyBase64.encode2utf(keyPair.getPublic().getEncoded());
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyClientCommand.HANDSHAKE;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(clientId)
				.append(publicKey)
				.append(reconnectToken)
				.append(clientType)
				.append(clientVersion)
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
					.keysize(512)
					.algorithm("RSA")
					.fileWriter(EzySimpleFileWriter.builder().build())
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
		return "";
//		return decryptToken();
	}
	
	protected String decryptToken() {
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
				.fileReader(EzySimpleFileReader.builder().build())
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
