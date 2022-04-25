package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EzyBlockingSocketDisconnectionQueue
    extends EzyLoggable
    implements EzySocketDisconnectionQueue {

    private final BlockingQueue<EzySocketDisconnection> queue =
        new LinkedBlockingQueue<>();

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
        //noinspection ResultOfMethodCallIgnored
        queue.remove(disconnection);
    }

    @Override
    public EzySocketDisconnection take() throws InterruptedException {
        return queue.take();
    }
}
