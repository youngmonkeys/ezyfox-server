package com.tvd12.ezyfoxserver.response;

public interface EzyResponseParamsFetcher<P extends EzyResponseParams> {

    P getParams();
}
