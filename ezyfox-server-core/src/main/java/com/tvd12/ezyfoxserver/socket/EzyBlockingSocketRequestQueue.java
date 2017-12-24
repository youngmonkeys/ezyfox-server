package com.tvd12.ezyfoxserver.socket;

import java.util.concurrent.BlockingQueue;

import com.tvd12.ezyfoxserver.util.EzyLoggable;

public abstract class EzyBlockingSocketRequestQueue 
        extends EzyLoggable 
        implements EzySocketRequestQueue {

    private final int capacity;
	private final BlockingQueue<EzySocketRequest> queue;
	
	public EzyBlockingSocketRequestQueue() {
	    this(10000);
	}
	
	public EzyBlockingSocketRequestQueue(int capacity) {
	    this.capacity = capacity;
        this.queue = newQueue(capacity);
	}
	
	protected abstract BlockingQueue<EzySocketRequest> newQueue(int capacity);

	@Override
	public int size() {
	    return queue.size();
	}
	
	@Override
	public void clear() {
		queue.clear();
	}
	
	@Override
	public boolean isFull() {
	    return queue.size() >= capacity;
	}
	
	@Override
	public boolean isEmpty() {
	    return queue.isEmpty();
	}
	
	@Override
	public boolean add(EzySocketRequest request) {
	    getLogger().info("add request, size = {}, full = {}", queue.size(), isFull());
	    if(queue.size() >= capacity)
	        return false;
		return queue.offer(request);
	}
	
	@Override
	public void remove(EzySocketRequest request) {
	    queue.remove(request);
	}
	
    @Override
	public EzySocketRequest take() throws InterruptedException {
        EzySocketRequest request = queue.take();
		return request;
	}
	
}
