package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfox.sercurity.EzyBase64;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyHandShakeParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = 6597013677969259046L;

    protected String token;
    protected byte[] clientKey;
    protected byte[] serverPublicKey;
	
	@Override
	protected EzyArrayBuilder serialize0() {
	    String encryptedServerPublicKey = encryptServerPublicKey();
	    return newArrayBuilder()
            .append(encryptedServerPublicKey)
            .append(token);
	}
	
	protected String encryptServerPublicKey() {
	    if(serverPublicKey != null)
	        return EzyBase64.encode2utf(serverPublicKey);
	    return "";
    } 
	
}
