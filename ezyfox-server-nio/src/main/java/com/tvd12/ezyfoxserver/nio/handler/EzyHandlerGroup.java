package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;

public interface EzyHandlerGroup {

	void fireChannelInactive();
	
	void fireChannelInactive(EzyConstant reason);
	
	void fireExceptionCaught(Throwable throwable);
	
	void fireDataSend(Object data) throws Exception;
	
	EzyNioSession fireChannelActive() throws Exception;
	
}
