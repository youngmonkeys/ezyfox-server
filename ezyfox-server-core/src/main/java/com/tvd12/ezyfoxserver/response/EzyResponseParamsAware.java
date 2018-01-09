package com.tvd12.ezyfoxserver.response;

public interface EzyResponseParamsAware<P extends EzyResponseParams> {

    void setParams(P params);
    
}
