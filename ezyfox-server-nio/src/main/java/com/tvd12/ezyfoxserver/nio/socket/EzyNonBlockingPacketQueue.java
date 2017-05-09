package com.tvd12.ezyfoxserver.nio.socket;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EzyNonBlockingPacketQueue implements EzyPacketQueue {

	private final int capacity;
	private final Queue<EzyPacket> queue = new ConcurrentLinkedQueue<>();
	
	public EzyNonBlockingPacketQueue() {
		this(512);
	}
	
	public EzyNonBlockingPacketQueue(int capacity) {
		this.capacity = capacity;
	}
	
	@Override
	public void clear() {
		queue.clear();
	}
	
	@Override
	public EzyPacket take() {
		return queue.poll();
	}
	
	@Override
	public boolean isFull() {
		return queue.size() == capacity; 
	}
	
	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	@Override
	public void add(EzyPacket packet) {
		queue.add(packet);
	}
	
}
