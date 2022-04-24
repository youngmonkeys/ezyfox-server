package com.tvd12.ezyfoxserver.nio.udp;

import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopOneHandler;

public class EzyNioUdpReadingLoopHandler extends EzySocketEventLoopOneHandler {

    @Override
    protected String getThreadName() {
        return "udp-reader";
    }
}
