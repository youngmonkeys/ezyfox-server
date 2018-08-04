package com.tvd12.ezyfoxserver.request;

import java.io.Serializable;

import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.entity.EzySessionFetcher;

public interface EzyRequest<P extends EzyRequestParams> extends 
        EzySessionFetcher,
        EzyRequestParamsFetcher<P>,
        EzyReleasable,
        Serializable {
}
