package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManagerAware;
import com.tvd12.ezyfoxserver.socket.EzySocketWriter;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroup;

import lombok.Setter;

public class EzyNioSocketWriter 
		extends EzySocketWriter
		implements EzyHandlerGroupManagerAware {

	@Setter
	protected EzyHandlerGroupManager handlerGroupManager;
	
	@Override
	protected EzySocketWriterGroup getWriterGroup(EzySession session) {
		Object connection = ((EzyNioSession)session).getConnection();
		return connection == null ? null : handlerGroupManager.getHandlerGroup(connection);
	}

}
