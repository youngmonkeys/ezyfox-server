package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public class EzyAccessAppErrorResponse extends EzyErrorResponse {

    protected EzyAccessAppErrorResponse(Builder builder) {
        super(builder);
    }
    
    @Override
    public EzyConstant getCommand() {
        return EzyCommand.APP_ACCESS_ERROR;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzyErrorResponse.Builder<Builder> {
        @Override
        public EzyAccessAppErrorResponse build() {
            return new EzyAccessAppErrorResponse(this);
        }
    }
    
}
