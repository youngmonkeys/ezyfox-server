package com.tvd12.ezyfoxserver.request;

public class EzySimpleLoginRequest
        extends EzySimpleRequest<EzyLoginParams>
        implements EzyLoginRequest {
    private static final long serialVersionUID = -4083284406277168017L;
    
    @Override
    protected EzyLoginParams newParams() {
        return new EzySimpleLoginParams();
    }
    
}
