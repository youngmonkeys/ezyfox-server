package com.tvd12.ezyfoxserver.response;

import lombok.Getter;

@Getter
public abstract class EzyFixedCommandResponse 
		extends EzyBaseResponse 
		implements EzyResponse {

    protected Object data;
    
    protected EzyFixedCommandResponse(Builder<?> builder) {
        super(builder);
        this.data = builder.data;
    }
    
    @SuppressWarnings("unchecked")
    public static abstract class Builder<B extends Builder<B>>
            extends EzyBaseResponse.Builder<Builder<B>> {
        
        protected Object data;
        
        public B data(Object data) {
            this.data = data;
            return (B)this;
        }
        
    }
	
}
