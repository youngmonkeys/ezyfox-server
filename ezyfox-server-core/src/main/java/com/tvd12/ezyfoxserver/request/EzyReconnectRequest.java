package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyReconnectRequest extends EzyRequest<EzyReconnectParams> {
    
    EzySession getOldSession();
    
}
