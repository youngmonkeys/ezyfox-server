package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;

import lombok.Getter;

@Getter
public class EzySimpleUserSessionEvent implements EzyUserSessionEvent {

    protected EzyUser user;
    protected EzySession session;
    
    protected EzySimpleUserSessionEvent(Builder<?> builder) {
        this.user = builder.user;
        this.session = builder.session;
    }
    
    public static class Builder<B extends Builder<B>> implements EzyBuilder<EzyEvent> {
    
        protected EzyUser user;
        protected EzySession session;
        
        @SuppressWarnings("unchecked")
        public B user(EzyUser user) {
            this.user = user;
            return (B)this;
        }
        
        @SuppressWarnings("unchecked")
        public B session(EzySession session) {
            this.session = session;
            return (B)this;
        }
        
        @Override
        public EzyEvent build() {
            return new EzySimpleUserSessionEvent(this);
        }
    }
    
}
