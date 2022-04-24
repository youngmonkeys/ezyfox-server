package com.tvd12.ezyfoxserver.socket;

public class EzySocketStreamHandlingLoopHandler extends EzySocketEventLoopOneHandler {

    @Override
    protected final String getThreadName() {
        return "socket-stream-handler";
    }
}
