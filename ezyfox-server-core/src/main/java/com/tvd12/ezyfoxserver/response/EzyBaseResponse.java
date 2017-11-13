package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;

public class EzyBaseResponse extends EzyEntityBuilders {

    public EzyBaseResponse(Builder<?> builder) {
    }
    
    public static abstract class Builder<B extends Builder<B>> 
            implements EzyResponse.Builder {
    }
    
}
