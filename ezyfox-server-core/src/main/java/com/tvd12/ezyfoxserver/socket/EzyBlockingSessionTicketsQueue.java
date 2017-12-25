package com.tvd12.ezyfoxserver.socket;

import java.util.concurrent.LinkedBlockingQueue;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyBlockingSessionTicketsQueue 
        extends EzyLoggable 
        implements EzySessionTicketsQueue {

    private final int capacity;
	private final LinkedBlockingQueue<EzySession> queue;
	
	public EzyBlockingSessionTicketsQueue() {
	    this(100000);
	}
	
	public EzyBlockingSessionTicketsQueue(int capacity) {
	    this.capacity = capacity;
        this.queue = new LinkedBlockingQueue<>(capacity);
	}

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
	public boolean add(EzySession session) {
		return queue.offer(session);
	}
	
	@Override
	public void remove(EzySession session) {
	    queue.remove(session);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public <T extends EzySession> T take() throws InterruptedException {
		T session = (T) queue.take();
		return session;
	}
	
}
