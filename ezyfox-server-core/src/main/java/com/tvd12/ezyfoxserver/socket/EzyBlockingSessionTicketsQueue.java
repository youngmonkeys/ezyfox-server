package com.tvd12.ezyfoxserver.socket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyBlockingSessionTicketsQueue 
        extends EzyLoggable 
        implements EzySessionTicketsQueue {

    private final int capacity;
	private final LinkedBlockingQueue<EzySession> queue;
	private final ConcurrentHashMap<EzySession, Integer> sessionCounts;
	
	public EzyBlockingSessionTicketsQueue() {
	    this(201800);
	}
	
	public EzyBlockingSessionTicketsQueue(int capacity) {
	    this.capacity = capacity;
	    this.sessionCounts = new ConcurrentHashMap<>();
        this.queue = new LinkedBlockingQueue<>();
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
	    if(hasSessionInQueue(session))
            return false;
	    updateSessionCount(session, 1);
		queue.offer(session);
		return true;
	}
	
	public boolean hasSessionInQueue(EzySession session) {
	    synchronized (sessionCounts) {
	        Integer count = sessionCounts.get(session);
            return count != null && count > 0; 
        }
	}
	
	public void updateSessionCount(EzySession session, int offset) {
	    synchronized (sessionCounts) {
	        sessionCounts.compute(
	                session, (k, v) -> v != null ? v + offset : offset);
        }
	}
	
	@Override
	public void remove(EzySession session) {
	    queue.remove(session);
	    sessionCounts.remove(session);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public <T extends EzySession> T take() throws InterruptedException {
		T session = (T) queue.take();
		updateSessionCount(session, -1);
		return session;
	}
	
}
