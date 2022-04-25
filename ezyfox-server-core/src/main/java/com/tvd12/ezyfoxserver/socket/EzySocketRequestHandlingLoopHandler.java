package com.tvd12.ezyfoxserver.socket;

public abstract class EzySocketRequestHandlingLoopHandler extends EzySocketEventLoopOneHandler {

    @Override
    protected final String getThreadName() {
        return getRequestType() + "-request-handler";
    }

    protected abstract String getRequestType();
}
