package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleAccessAppParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleHandShakeParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleLoginParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimplePingParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequestAppParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequestPluginParams;
import com.tvd12.ezyfoxserver.wrapper.EzyRequestMappers;

public class EzyRequestMappersImpl implements EzyRequestMappers {

    @SuppressWarnings("rawtypes")
    private final Map<EzyConstant, Function> appliers; 
    
    protected EzyRequestMappersImpl(Builder builder) {
        this.appliers = builder.newAppliers();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(EzyConstant cmd, EzyData data) {
        return (T) appliers.get(cmd).apply(data);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyRequestMappers> {

        @Override
        public EzyRequestMappers build() {
            return new EzyRequestMappersImpl(this);
        }
        
        @SuppressWarnings("rawtypes")
        protected Map<EzyConstant, Function> newAppliers() {
            Map<EzyConstant, Function> answer = new ConcurrentHashMap<>();
            answer.put(EzyCommand.PING, new Function<EzyArray, Object>() {
                @Override
                public Object apply(EzyArray t) {
                    return EzySimplePingParams.builder().build();
                };
            });
            answer.put(EzyCommand.HANDSHAKE, new Function<EzyArray, Object>() {
                @Override
                public Object apply(EzyArray t) {
                    return EzySimpleHandShakeParams.builder()
                            .clientId(t.get(0, String.class))
                            .clientKey(t.get(1, String.class))
                            .reconnectToken(t.get(2, String.class))
                            .clientType(t.get(3, String.class))
                            .clientVersion(t.get(4, String.class))
                            .build();
                }
            });
            answer.put(EzyCommand.LOGIN, new Function<EzyArray, Object>() {
                @Override
                public Object apply(EzyArray t) {
                    return EzySimpleLoginParams.builder()
                            .username(t.get(0, String.class))
                            .password(t.get(1, String.class))
                            .data(t.get(2, EzyArray.class))
                            .build();
                }
            });
            answer.put(EzyCommand.APP_ACCESS, new Function<EzyArray, Object>() {
                @Override
                public Object apply(EzyArray t) {
                    return EzySimpleAccessAppParams.builder()
                            .appName(t.get(0, String.class))
                            .build();
                }
            });
            answer.put(EzyCommand.APP_REQUEST, new Function<EzyArray, Object>() {
                @Override
                public Object apply(EzyArray t) {
                    return EzySimpleRequestAppParams.builder()
                            .appId(t.get(0, int.class))
                            .data(t.get(1, EzyArray.class))
                            .build();
                }
            });
            answer.put(EzyCommand.PLUGIN_REQUEST, new Function<EzyArray, Object>() {
                @Override
                public Object apply(EzyArray t) {
                    return EzySimpleRequestPluginParams.builder()
                            .pluginName(t.get(0, String.class))
                            .data(t.get(1, EzyArray.class))
                            .build();
                }
            });
            return answer;
        }
        
    }
    
}
