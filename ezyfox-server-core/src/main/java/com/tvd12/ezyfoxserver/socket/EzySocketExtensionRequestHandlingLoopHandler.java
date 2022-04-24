package com.tvd12.ezyfoxserver.socket;

public class EzySocketExtensionRequestHandlingLoopHandler extends EzySocketRequestHandlingLoopHandler {

    @Override
    protected String getRequestType() {
        return "extension";
    }
}
