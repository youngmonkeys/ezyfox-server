package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public class EzyLoginErrorResponse extends EzyErrorResponse {

    protected EzyLoginErrorResponse(Builder builder) {
        super(builder);
    }
    
    @Override
    public EzyConstant getCommand() {
        return EzyCommand.LOGIN_ERROR;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzyErrorResponse.Builder<Builder> {
        @Override
        public EzyLoginErrorResponse build() {
            return new EzyLoginErrorResponse(this);
        }
    }
    
}
