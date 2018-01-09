package com.tvd12.ezyfoxserver.request;

public interface EzyRequestParamsFetcher<P extends EzyRequestParams> {

    P getParams();
    
}
