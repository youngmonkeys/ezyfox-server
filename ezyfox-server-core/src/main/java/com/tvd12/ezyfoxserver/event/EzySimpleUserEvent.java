package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.Getter;

@Getter
public class EzySimpleUserEvent implements EzyUserEvent {

    protected EzyUser user;
    
    protected EzySimpleUserEvent(Builder<?> builder) {
        this.user = builder.user;
    }
    
    public static class Builder<B extends Builder<B>> implements EzyBuilder<EzyEvent> {
    
        protected EzyUser user;
        
        @SuppressWarnings("unchecked")
        public B user(EzyUser user) {
            this.user = user;
            return (B)this;
        }
        
        @Override
        public EzyEvent build() {
            return new EzySimpleUserEvent(this);
        }
    }
    
}
