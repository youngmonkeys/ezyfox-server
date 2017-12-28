package com.tvd12.ezyfoxserver.netty.handler;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandler;

public interface EzyNettyDataHandler extends EzySocketDataHandler {

	public void channelInactive() throws Exception;
	
	public EzyNettySession channelActive() throws Exception;
	
    public void channelInactive(EzyConstant reason) throws Exception;
    
    public void exceptionCaught(Throwable throwable)  throws Exception;
	
}
