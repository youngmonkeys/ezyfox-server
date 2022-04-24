package com.tvd12.ezyfoxserver.interceptor;

public interface EzyInterceptor<C, R> {

    @SuppressWarnings("rawtypes")
    EzyInterceptor ALWAYS_PASS = (ctx, request) -> {};

    void intercept(C ctx, R request) throws Exception;
}
