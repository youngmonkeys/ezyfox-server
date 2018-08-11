package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

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
