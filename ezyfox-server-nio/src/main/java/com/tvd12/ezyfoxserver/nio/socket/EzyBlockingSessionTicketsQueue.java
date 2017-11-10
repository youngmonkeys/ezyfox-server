package com.tvd12.ezyfoxserver.nio.socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;

public class EzyBlockingSessionTicketsQueue implements EzySessionTicketsQueue {

	private final BlockingQueue<EzyNioSession> queue 
			= new LinkedBlockingQueue<>();

	@Override
	public void clear() {
		queue.clear();
	}
	
	@Override
	public void add(EzyNioSession session) {
		queue.add(session);
	}
	
	@Override
	public EzyNioSession take() throws InterruptedException {
		return queue.take();
	}
	
}
