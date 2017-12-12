package com.tvd12.ezyfoxserver.netty.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.socket.EzySocketWriter;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroup;

public class EzyNettySocketWriter extends EzySocketWriter {

	@Override
	protected EzySocketWriterGroup getWriterGroup(EzySession session) {
		return ((EzyNettySession)session).getHandlerGroup();
	}

}
