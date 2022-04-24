package com.tvd12.ezyfoxserver.response;

public interface EzyParamsResponse<P extends EzyResponseParams>
    extends EzyResponse, EzyResponseParamsFetcher<P> {
}
