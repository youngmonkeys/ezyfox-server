package com.tvd12.ezyfoxserver.socket;

public interface EzySocketRequestQueue {

    int size();
    
	void clear();
	
	boolean isFull();
	
	boolean isEmpty();
	
	boolean add(EzySocketRequest request);
	
	void remove(EzySocketRequest request);
	
	EzySocketRequest take() throws InterruptedException;
	
}
