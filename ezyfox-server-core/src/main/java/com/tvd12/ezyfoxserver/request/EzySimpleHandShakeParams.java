package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyArrayDeserializable;

import lombok.Getter;

@Getter
public class EzySimpleHandShakeParams implements EzyHandshakeParams, EzyArrayDeserializable {
    private static final long serialVersionUID = 8042927639638762414L;
    
    protected String clientId;
    protected String clientKey;
    protected String clientType;
    protected String clientVersion;
    protected String reconnectToken;
    
    @Override
    public void deserialize(EzyArray t) {
        this.clientId = t.get(0, String.class);
        this.clientKey = t.get(1, String.class);
        this.reconnectToken = t.get(2, String.class);
        this.clientType = t.get(3, String.class);
        this.clientVersion = t.get(4, String.class);
    }
    
}
