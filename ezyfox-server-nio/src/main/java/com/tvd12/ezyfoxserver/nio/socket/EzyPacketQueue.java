package com.tvd12.ezyfoxserver.nio.socket;

public interface EzyPacketQueue {

	void clear();
	
	EzyPacket take();
	
	boolean isFull();
	
	boolean isEmpty();

	void add(EzyPacket packet);
	
}
