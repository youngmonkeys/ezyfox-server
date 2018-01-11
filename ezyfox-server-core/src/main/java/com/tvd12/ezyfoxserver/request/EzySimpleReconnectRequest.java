package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzySimpleReconnectRequest
        extends EzySimpleRequest<EzyReconnectParams>
        implements EzyReconnectRequest {
    private static final long serialVersionUID = -6942371514102761780L;
    
    protected EzySession oldSession;
    
    @Override
    protected EzyReconnectParams newParams() {
        return new EzySimpleReconnectParams();
    }
    
    @Override
    public void release() {
        super.release();
        this.oldSession = null;
    }
    
}
