package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;

public interface EzyHandlerGroup {

	void fireChannelInactive();
	
	void fireExceptionCaught(Throwable throwable);

	void fireDataSend(Object data) throws Exception;
	
	EzyNioSession fireChannelActive() throws Exception;
	
	void fireChannelInactive(EzyDisconnectReason reason);
	
}
