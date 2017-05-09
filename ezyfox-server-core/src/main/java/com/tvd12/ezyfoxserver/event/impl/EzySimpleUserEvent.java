package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzyUserEvent;

import lombok.Getter;

@Getter
public class EzySimpleUserEvent implements EzyUserEvent {

    protected EzyUser user;
    
    public static class Builder<B extends Builder<B>> implements EzyBuilder<EzyEvent> {
    
        protected EzyUser user;
        
        @SuppressWarnings("unchecked")
        public B user(EzyUser user) {
            this.user = user;
            return (B)this;
        }
        
        @Override
        public EzyEvent build() {
            EzySimpleUserEvent answer = newProduct();
            answer.user = user;
            return answer;
        }
        
        protected EzySimpleUserEvent newProduct() {
            return new EzySimpleUserEvent();
        }
    }
    
}
