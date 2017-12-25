package com.tvd12.ezyfoxserver.socket;

import java.util.LinkedList;
import java.util.Queue;

import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyNonBlockingPacketQueue extends EzyLoggable implements EzyPacketQueue {

	private final int capacity;
	private final Queue<EzyPacket> queue ;
	
	public EzyNonBlockingPacketQueue() {
		this(256);
	}
	
	public EzyNonBlockingPacketQueue(int capacity) {
		this.capacity = capacity;
		this.queue = new LinkedList<>();
	}
	
	@Override
	public int size() {
	    synchronized (queue) {
	        return queue.size();
        }
	}
	
	@Override
	public void clear() {
	    synchronized (queue) {
	        queue.clear();
	    }
	}
	
	@Override
	public EzyPacket take() {
	    synchronized (queue) {
	        EzyPacket packet = queue.poll();
	        return packet;
	    }
	}
	
	@Override
	public EzyPacket peek() {
	    synchronized (queue) {
            EzyPacket packet = queue.peek();
            return packet;
        }
	}
	
	@Override
	public boolean isFull() {
	    synchronized (queue) {
	        return queue.size() >= capacity;
	    }
	}
	
	@Override
	public boolean isEmpty() {
	    synchronized (queue) {
	        return queue.isEmpty();
	    }
	}
	
	@Override
	public boolean add(EzyPacket packet) {
	    synchronized (queue) {
	        if(queue.size() >= capacity) 
	            return false;
	        queue.offer(packet);
	        return true;
	    }
	}
	
}
