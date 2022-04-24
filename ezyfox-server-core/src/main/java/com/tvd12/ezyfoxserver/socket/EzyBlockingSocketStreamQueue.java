package com.tvd12.ezyfoxserver.socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.tvd12.ezyfox.util.EzyLoggable;

public class EzyBlockingSocketStreamQueue 
        extends EzyLoggable 
        implements EzySocketStreamQueue {

    private final int capacity;
    private final BlockingQueue<EzySocketStream> queue;

    public EzyBlockingSocketStreamQueue() {
        this(50000);
    }

    public EzyBlockingSocketStreamQueue(int capacity) {
        this.capacity = capacity;
        this.queue = newQueue(capacity);
    }

    protected BlockingQueue<EzySocketStream> newQueue(int capacity) {
        return new LinkedBlockingDeque<>(capacity);
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
    public boolean add(EzySocketStream stream) {
        if(queue.size() >= capacity)
            return false;
        return queue.offer(stream);
    }

    @Override
    public void remove(EzySocketStream stream) {
        queue.remove(stream);
    }

    @Override
    public EzySocketStream take() throws InterruptedException {
        EzySocketStream stream = queue.take();
        return stream;
    }

}
