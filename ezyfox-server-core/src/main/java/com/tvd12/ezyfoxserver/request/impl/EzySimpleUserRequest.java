package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.request.EzyUserRequest;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleUserRequest<P> 
        extends EzySimpleRequest<P>
        implements EzyUserRequest<P> {

    protected EzyUser user;
    
    protected EzySimpleUserRequest(Builder<P, ?> builder) {
        super(builder);
        this.user = builder.user;
    }
    
    public abstract static class Builder<P,B extends Builder<P, B>>
            extends EzySimpleRequest.Builder<P, B> {
        
        protected EzyUser user;
        
        @SuppressWarnings("unchecked")
        public B user(EzyUser user) {
            this.user = user;
            return (B)this;
        }
        
        @Override
        public abstract EzyUserRequest<P> build();
    }
    
}
