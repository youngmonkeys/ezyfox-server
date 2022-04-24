package com.tvd12.ezyfoxserver.socket;

public class EzySocketWritingLoopHandler extends EzySocketEventLoopMultiHandler {

    @Override
    protected String getThreadName() {
        return "socket-writer";
    }

}
