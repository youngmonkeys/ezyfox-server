package com.tvd12.ezyfoxserver.nio.protocol;

import com.tvd12.ezyfoxserver.socket.EzySocketWritingLoopHandler;

public class EzyProtocolWritingLoopHandler extends EzySocketWritingLoopHandler {

    @Override
    protected String getThreadName() {
        return "protocol-writer";
    }
}
