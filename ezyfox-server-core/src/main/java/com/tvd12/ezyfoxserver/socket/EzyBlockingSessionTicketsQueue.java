package com.tvd12.ezyfoxserver.socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.tvd12.ezyfoxserver.entity.EzySession;

public class EzyBlockingSessionTicketsQueue implements EzySessionTicketsQueue {

	private final BlockingQueue<EzySession> queue 
			= new LinkedBlockingQueue<>();

	@Override
	public void clear() {
		queue.clear();
	}
	
	@Override
	public boolean add(EzySession session) {
		return queue.offer(session);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public <T extends EzySession> T take() throws InterruptedException {
		return (T) queue.take();
	}
	
}
