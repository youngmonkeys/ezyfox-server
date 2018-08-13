package com.tvd12.ezyfoxserver.socket;

public interface EzySocketDisconnectionQueue {

    int size();
    
	void clear();
	
	boolean isEmpty();
	
	boolean add(EzySocketDisconnection disconnection);
	
	void remove(EzySocketDisconnection disconnection);
	
	EzySocketDisconnection take() throws InterruptedException;
	
}
