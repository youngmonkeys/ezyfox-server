package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class EzySocketRequestBuilder implements EzyBuilder<EzySocketRequest> {

    private EzyArray data;
    private EzySocketDataHandler handler;
    
    public static EzySocketRequestBuilder socketRequestBuilder() {
        return new EzySocketRequestBuilder(); 
    }
    
    public EzySocketRequestBuilder data(EzyArray data) {
        this.data = data;
        return this;
    }
    
    public EzySocketRequestBuilder handler(EzySocketDataHandler handler) {
        this.handler = handler;
        return this;
    }
    
    @Override
    public EzySocketRequest build() {
        EzySimpleSocketRequest request = new EzySimpleSocketRequest(data);
        request.setHandler(handler);
        return request;
    }
    
}
