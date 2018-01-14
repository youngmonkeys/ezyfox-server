package com.tvd12.ezyfoxserver.api;

@SuppressWarnings("rawtypes")
public interface EzyApis {

    <T> T getApi(Class<T> apiClass);
    
    void addApi(Class clazz, Object instance);
    
}
