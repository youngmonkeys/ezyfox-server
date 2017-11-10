package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyRequest<P> {

    P getParams();
    
    EzySession getSession();
    
}
