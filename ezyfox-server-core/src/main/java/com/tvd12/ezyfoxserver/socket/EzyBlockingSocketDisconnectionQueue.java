package com.tvd12.ezyfoxserver.socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.tvd12.ezyfox.util.EzyLoggable;

public class EzyBlockingSocketDisconnectionQueue 
        extends EzyLoggable 
        implements EzySocketDisconnectionQueue {

	private final BlockingQueue<EzySocketDisconnection> queue;
	private static final EzyBlockingSocketDisconnectionQueue INSTANCE = new EzyBlockingSocketDisconnectionQueue();
	
	private EzyBlockingSocketDisconnectionQueue() {
	    this.queue = new LinkedBlockingQueue<EzySocketDisconnection>();
	}
	
	public static EzyBlockingSocketDisconnectionQueue getInstance() {
	    return INSTANCE;
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
	public boolean isEmpty() {
	    return queue.isEmpty();
	}
	
	@Override
	public boolean add(EzySocketDisconnection disconnection) {
		return queue.offer(disconnection);
	}
	
	@Override
	public void remove(EzySocketDisconnection disconnection) {
	    queue.remove(disconnection);
	}
	
    @Override
	public EzySocketDisconnection take() throws InterruptedException {
        EzySocketDisconnection disconnection = queue.take();
		return disconnection;
	}
	
}
