package com.tvd12.ezyfoxserver.socket;

public class EzySocketExtensionRequestHandler extends EzySocketRequestHandler {

    @Override
    protected String getRequestType() {
        return "extension";
    }
    
}
