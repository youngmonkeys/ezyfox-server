package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySessionTicketsQueue {

	void clear();
	
	boolean add(EzySession session);
	
	<T extends EzySession> T take() throws InterruptedException;
	
}
