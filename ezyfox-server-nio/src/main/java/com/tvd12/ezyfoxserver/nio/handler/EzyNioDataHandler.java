package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandler;

public interface EzyNioDataHandler extends EzySocketDataHandler {

	public void channelInactive(EzyConstant disconnectReason);
	
    public void exceptionCaught(Throwable throwable)  throws Exception;
	
}
