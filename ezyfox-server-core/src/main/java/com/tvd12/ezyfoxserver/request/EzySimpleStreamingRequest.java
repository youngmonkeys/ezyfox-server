package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzySimpleStreamingRequest implements EzyStreamingRequest {

    private EzyUser user;
    private EzySession session;
    private byte[] bytes;
    
    @Override
    public void release() {
        this.user = null;
        this.session = null;
        this.bytes = null;
    }
    
}
