package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;

public interface EzySessionTicketsQueue {

	void clear();
	
	void add(EzyNioSession session);
	
	EzyNioSession take() throws InterruptedException;
	
}
