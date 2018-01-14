package com.tvd12.ezyfoxserver.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class EzySimpleApis implements EzyApis {

    protected Map<Class, Object> apis = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getApi(Class<T> apiClass) {
        return (T) apis.get(apiClass);
    }
    
    @Override
    public void addApi(Class clazz, Object instance) {
        apis.put(clazz, instance);
    }
    
}
