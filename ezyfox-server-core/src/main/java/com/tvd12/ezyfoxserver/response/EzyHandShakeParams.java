package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfox.sercurity.EzyAsyCrypt;
import com.tvd12.ezyfox.sercurity.EzyBase64;
import com.tvd12.ezyfox.util.EzyInitable;
import com.tvd12.ezyfox.util.EzyReturner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyHandShakeParams 
        extends EzySimpleResponseParams
        implements EzyInitable {
    private static final long serialVersionUID = 6597013677969259046L;
    
    protected byte[] clientKey;
    protected String reconnectToken;
    protected byte[] serverPublicKey;
    @Setter(AccessLevel.NONE)
	protected String encryptedServerPublicKey;
	
	@Override
	public void init() {
	    this.reconnectToken = encryptReconnectToken();
        this.encryptedServerPublicKey = encryptServerPublicKey();
	}
	
	@Override
	protected EzyArrayBuilder serialize0() {
	    this.preserialize();
	    return newArrayBuilder()
            .append(encryptedServerPublicKey)
            .append(reconnectToken);
	}
	
	protected void preserialize() {
	    if(encryptedServerPublicKey == null)
            init();
	}
	
	protected String encryptServerPublicKey() {
        return EzyBase64.encode2utf(serverPublicKey); 
    } 
     
    protected String encryptReconnectToken() {
        return encryptReconnectToken(clientKey, reconnectToken); 
    } 
     
    protected String encryptReconnectToken(byte[] key, String token) {
        return EzyReturner.returnWithException( 
                () -> encryptReconnectToken0(key, token), 
                (e) -> new IllegalArgumentException(e));
    } 
     
    protected String encryptReconnectToken0(byte[] key, String token) 
            throws Exception {
        return EzyAsyCrypt.builder() 
                .algorithm("RSA") 
                .publicKey(key)
                .build().encrypt(token, String.class);
    } 
	
}
