package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfoxserver.socket.EzySocketWritingLoopHandler;

public class EzyWsWritingLoopHandler extends EzySocketWritingLoopHandler {

    @Override
    protected String getThreadName() {
        return "web-socket-writer";
    }

}
