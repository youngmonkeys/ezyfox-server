package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopOneHandler;

public class EzyNioSocketAcceptanceLoopHandler extends EzySocketEventLoopOneHandler {

    @Override
    protected String getThreadName() {
        return "socket-acceptor";
    }

}
