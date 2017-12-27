package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandler;

public interface EzyNioDataHandler extends EzySocketDataHandler {

	public void channelInactive() throws Exception;
	
	public EzyNioSession channelActive() throws Exception;
	
    public void channelInactive(EzyConstant reason) throws Exception;
    
    public void exceptionCaught(Throwable throwable)  throws Exception;
	
}
