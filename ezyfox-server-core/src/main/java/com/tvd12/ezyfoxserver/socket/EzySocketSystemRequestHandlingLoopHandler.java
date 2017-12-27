package com.tvd12.ezyfoxserver.socket;

public class EzySocketSystemRequestHandlingLoopHandler extends EzySocketRequestHandlingLoopHandler {

    @Override
    protected String getRequestType() {
        return "system";
    }
    
}
