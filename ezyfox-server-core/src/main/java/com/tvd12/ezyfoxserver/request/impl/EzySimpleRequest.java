package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyRequest;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleRequest<P> implements EzyRequest<P> {

    protected P params;
    protected EzySession session;
    
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
            EzySimpleRequest<P> answer = newProduct();
            answer.setParams(params);
            answer.setSession(session);
            return answer;
        }
        
        protected abstract EzySimpleRequest<P> newProduct();
        
    }
    
}
