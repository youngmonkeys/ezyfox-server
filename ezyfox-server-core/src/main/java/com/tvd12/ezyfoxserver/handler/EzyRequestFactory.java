package com.tvd12.ezyfoxserver.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleAccessAppRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleHandShakeRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleLoginRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimplePingRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequestAppRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequestPluginRequest;

public class EzyRequestFactory {
    
    protected Supplier<EzySession> sessionSupplier;
    
    @SuppressWarnings("rawtypes")
    protected Map<EzyConstant, Supplier<EzySimpleRequest.Builder>> suppliers;
    
    protected EzyRequestFactory(Builder builder) {
        this.suppliers = builder.newSuppliers();
        this.sessionSupplier = builder.sessionSupplier;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object newRequest(EzyConstant cmd, Object params) {
        EzySimpleRequest.Builder builder = suppliers.get(cmd).get();
        EzySession session = sessionSupplier.get();
        return builder.params(params).session(session).build();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
            implements EzyBuilder<EzyRequestFactory> {
        
        protected Supplier<EzyUser> userSupplier;
        protected Supplier<EzySession> sessionSupplier;
        
        public Builder userSupplier(Supplier<EzyUser> userSupplier) {
            this.userSupplier = userSupplier;
            return this;
        }
        
        public Builder sessionSupplier(Supplier<EzySession> sessionSupplier) {
            this.sessionSupplier = sessionSupplier;
            return this;
        }
        
        @Override
        public EzyRequestFactory build() {
            return new EzyRequestFactory(this);
        }
        
        protected EzyUser getUser() {
            return userSupplier.get();
        }
        
        @SuppressWarnings("rawtypes")
        protected Map<EzyConstant, Supplier<EzySimpleRequest.Builder>> newSuppliers() {
            Map<EzyConstant, Supplier<EzySimpleRequest.Builder>> answer = new ConcurrentHashMap<>();
            answer.put(EzyCommand.PING, () ->
                EzySimplePingRequest.builder()
            );
            answer.put(EzyCommand.HANDSHAKE, () ->
                EzySimpleHandShakeRequest.builder()
            );
            answer.put(EzyCommand.LOGIN, ()->
                EzySimpleLoginRequest.builder()
            );
            answer.put(EzyCommand.APP_ACCESS, () ->
                EzySimpleAccessAppRequest.builder().user(getUser())
            );
            answer.put(EzyCommand.APP_REQUEST, () ->
                EzySimpleRequestAppRequest.builder().user(getUser())
            );
            answer.put(EzyCommand.PLUGIN_REQUEST, () ->
                EzySimpleRequestPluginRequest.builder().user(getUser())
            );
            return answer;
        }
    }
}