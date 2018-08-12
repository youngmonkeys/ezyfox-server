package com.tvd12.ezyfoxserver.socket;

public interface EzySocketDisconnectionQueue {

    int size();
    
	void clear();
	
	boolean isEmpty();
	
	boolean add(EzySocketDisconnection request);
	
	void remove(EzySocketDisconnection request);
	
	EzySocketDisconnection take() throws InterruptedException;
	
}
