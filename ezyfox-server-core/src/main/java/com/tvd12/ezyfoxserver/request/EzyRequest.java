package com.tvd12.ezyfoxserver.request;

import java.io.Serializable;

import com.tvd12.ezyfoxserver.entity.EzySessionFetcher;
import com.tvd12.ezyfoxserver.util.EzyReleasable;

public interface EzyRequest<P extends EzyRequestParams> extends 
        EzySessionFetcher,
        EzyRequestParamsFetcher<P>,
        EzyReleasable,
        Serializable {
}
