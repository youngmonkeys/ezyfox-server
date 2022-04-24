package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.entity.EzySessionFetcher;

import java.io.Serializable;

public interface EzyRequest<P extends EzyRequestParams> extends
    EzySessionFetcher,
    EzyRequestParamsFetcher<P>,
    EzyReleasable,
    Serializable {
}
