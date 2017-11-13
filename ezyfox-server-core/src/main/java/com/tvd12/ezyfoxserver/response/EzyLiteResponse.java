package com.tvd12.ezyfoxserver.response;

public class EzyLiteResponse extends EzyAbstractResponse {

    protected EzyLiteResponse(Builder<?> builder) {
        super(builder);
    }
    
    public static abstract class Builder<B extends Builder<B>>
            extends EzyAbstractResponse.Builder<Builder<B>> {
        
        @Override
        public abstract EzyLiteResponse build();
        
    }
}
