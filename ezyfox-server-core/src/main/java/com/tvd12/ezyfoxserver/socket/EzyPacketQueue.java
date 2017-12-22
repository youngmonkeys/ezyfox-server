package com.tvd12.ezyfoxserver.socket;

public interface EzyPacketQueue {

    int size();
    
	void clear();
	
	EzyPacket take();
	
	boolean isFull();
	
	boolean isEmpty();

	boolean add(EzyPacket packet);
	
}
