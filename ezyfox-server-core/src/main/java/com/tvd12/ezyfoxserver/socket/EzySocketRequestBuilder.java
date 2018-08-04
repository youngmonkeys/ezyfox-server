package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;

public class EzySocketRequestBuilder implements EzyBuilder<EzySocketRequest> {

    private EzyArray data;
    private EzySession session;
    
    public static EzySocketRequestBuilder socketRequestBuilder() {
        return new EzySocketRequestBuilder(); 
    }
    
    public EzySocketRequestBuilder data(EzyArray data) {
        this.data = data;
        return this;
    }
    
    public EzySocketRequestBuilder session(EzySession session) {
        this.session = session;
        return this;
    }
    
    @Override
    public EzySocketRequest build() {
        EzySimpleSocketRequest request = new EzySimpleSocketRequest(data);
        request.setSession(session);
        return request;
    }
    
}
