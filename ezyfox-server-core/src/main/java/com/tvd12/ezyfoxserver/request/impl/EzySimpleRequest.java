package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyRequest;

import lombok.Getter;

@Getter
public class EzySimpleRequest<P> implements EzyRequest<P> {

    protected P params;
    protected EzySession session;
    
    protected EzySimpleRequest(Builder<P,?> builder) {
        this.params = builder.params;
        this.session = builder.session;
    }
    
    public abstract static class Builder<P,B extends Builder<P,B>> 
            implements EzyBuilder<EzyRequest<P>> {
        protected P params;
        protected EzySession session;
        
        @SuppressWarnings("unchecked")
        public B params(P params) {
            this.params = params;
            return (B)this;
        }
        
        @SuppressWarnings("unchecked")
        public B session(EzySession session) {
            this.session = session;
            return (B)this;
        }
        
        @Override
        public EzyRequest<P> build() {
            return new EzySimpleRequest<>(this);
        }
        
    }
    
}
