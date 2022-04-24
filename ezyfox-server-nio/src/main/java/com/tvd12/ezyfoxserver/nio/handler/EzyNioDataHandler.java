package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandler;

public interface EzyNioDataHandler extends EzySocketDataHandler {

    void channelInactive(EzyConstant disconnectReason);

    void exceptionCaught(Throwable throwable)  throws Exception;

}
