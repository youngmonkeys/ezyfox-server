package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyUserFetcher;

public interface EzyUserRequest<P extends EzyRequestParams> 
        extends EzyRequest<P>, EzyUserFetcher {
}
