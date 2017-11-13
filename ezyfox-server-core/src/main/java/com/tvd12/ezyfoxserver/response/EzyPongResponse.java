package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public class EzyPongResponse extends EzyBaseResponse implements EzyResponse {

    protected EzyPongResponse(Builder builder) {
        super(builder);
    }
    
    @Override
    public Object getData() {
        return newArrayBuilder().build();
    }

    @Override
    public EzyConstant getCommand() {
        return EzyCommand.PONG;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzyBaseResponse.Builder<Builder> {
        @Override
        public EzyResponse build() {
            return new EzyPongResponse(this);
        }
    }

}
