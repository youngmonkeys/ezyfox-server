package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.entity.EzyUserAware;
import com.tvd12.ezyfoxserver.entity.EzyUserFetcher;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class EzySimpleUserRequest<P extends EzyRequestParams> 
        extends EzySimpleRequest<P>
        implements EzyUserRequest<P>, EzyUserFetcher, EzyUserAware {
    private static final long serialVersionUID = 5385999480619015817L;

    protected EzyUser user;
    
}
