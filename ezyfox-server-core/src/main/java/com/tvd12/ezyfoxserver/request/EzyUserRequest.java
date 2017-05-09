package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyUserRequest<P> extends EzyRequest<P> {

    EzyUser getUser();
    
}
