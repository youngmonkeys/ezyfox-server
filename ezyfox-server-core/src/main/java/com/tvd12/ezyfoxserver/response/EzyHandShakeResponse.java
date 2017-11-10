package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.sercurity.EzyAsyCrypt;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.util.EzyReturner;

import lombok.Setter;

@Setter
public class EzyHandShakeResponse extends EzyBaseResponse implements EzyResponse {

	protected boolean reconnect;
	protected String reconnectToken;
	protected String serverPublicKey;
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.HANDSHAKE;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(serverPublicKey)
				.append(reconnectToken)
				.append(reconnect)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyResponse.Builder {
	    protected byte[] clientKey;
	    protected boolean reconnect;
	    protected String reconnectToken;
	    protected byte[] serverPublicKey;
	    
	    public Builder clientKey(byte[] clientKey) {
	        this.clientKey = clientKey;
	        return this;
	    }
		
		public Builder reconnect(boolean value) {
		    this.reconnect = value;
		    return this;
		}
		
		public Builder reconnectToken(String reconnectToken) {
			this.reconnectToken = reconnectToken;
			return this;
		}
		
		public Builder serverPublicKey(byte[] serverPublicKey) {
            this.serverPublicKey = serverPublicKey;
            return this;
        }
		
		@Override
		public EzyResponse build() {
		    EzyHandShakeResponse answer = new EzyHandShakeResponse();
		    answer.setReconnect(reconnect);
		    answer.setReconnectToken(encryptReconnectToken());
		    answer.setServerPublicKey(encryptServerPublicKey());
		    return answer;
		}
		
		protected String encryptServerPublicKey() {
	        return EzyBase64.encode2utf(serverPublicKey); 
	    } 
	     
	    protected String encryptReconnectToken() {
	        return encryptReconnectToken(clientKey, reconnectToken); 
	    } 
	     
	    protected String encryptReconnectToken(byte[] key, String token) {
	        return EzyReturner.returnWithException( 
	                () -> tryEncryptReconnectToken(key, token), 
	                (e) -> new IllegalArgumentException(e));
	    } 
	     
	    protected String tryEncryptReconnectToken(byte[] key, String token) 
	            throws Exception {
	        return EzyAsyCrypt.builder() 
	                .algorithm("RSA") 
	                .publicKey(key)
	                .build().encrypt(token, String.class);
	    } 
	}

}
