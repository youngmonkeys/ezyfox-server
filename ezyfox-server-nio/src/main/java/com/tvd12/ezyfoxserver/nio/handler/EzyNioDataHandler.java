package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;

public interface EzyNioDataHandler {

	public void channelInactive() throws Exception;
	
	public void channelRead(Object msg) throws Exception;
    
	public EzyNioSession channelActive() throws Exception;
	
    public void exceptionCaught(Throwable throwable)  throws Exception;
    
    public void channelInactive(EzyDisconnectReason reason) throws Exception;
	
}
