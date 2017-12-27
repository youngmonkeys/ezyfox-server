package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySessionTicketsQueue {

    int size();
    
	void clear();
	
	boolean isEmpty();
	
	boolean add(EzySession session);
	
	void remove(EzySession session);
	
	<T extends EzySession> T take() throws InterruptedException;
	
}
