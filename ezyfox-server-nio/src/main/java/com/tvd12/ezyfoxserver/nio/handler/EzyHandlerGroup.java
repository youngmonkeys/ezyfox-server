package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroup;

public interface EzyHandlerGroup extends 
			EzySocketDataHandlerGroup, 
			EzySocketWriterGroup, 
			EzyDestroyable {

	void fireChannelInactive();
	
	void fireChannelInactive(EzyConstant reason);
	
	void fireExceptionCaught(Throwable throwable);
	
	EzyNioSession fireChannelActive() throws Exception;
	
}
