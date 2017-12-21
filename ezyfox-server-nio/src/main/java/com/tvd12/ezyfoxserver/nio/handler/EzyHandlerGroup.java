package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroup;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public interface EzyHandlerGroup extends EzySocketWriterGroup, EzyDestroyable {

	void fireChannelInactive();
	
	void fireChannelInactive(EzyConstant reason);
	
	void fireExceptionCaught(Throwable throwable);
	
	EzyNioSession fireChannelActive() throws Exception;
	
}
