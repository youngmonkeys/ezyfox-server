package com.tvd12.ezyfoxserver.request;

public interface EzyRequestParamsAware<P extends EzyRequestParams> {

    void setParams(P params);

}
