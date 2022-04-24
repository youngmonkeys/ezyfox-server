package com.tvd12.ezyfoxserver.socket;

import java.util.LinkedList;
import java.util.Queue;

import com.tvd12.ezyfox.util.EzyLoggable;

public class EzyNonBlockingRequestQueue extends EzyLoggable implements EzyRequestQueue {

    private final int capacity;
    private final Queue<EzySocketRequest> queue ;
    
    public EzyNonBlockingRequestQueue() {
        this(128);
    }
    
    public EzyNonBlockingRequestQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
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
    public EzySocketRequest take() {
        return queue.poll();
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
    public boolean add(EzySocketRequest packet) {
        if(isFull()) 
            return false;
        return queue.offer(packet);
    }
    
}
