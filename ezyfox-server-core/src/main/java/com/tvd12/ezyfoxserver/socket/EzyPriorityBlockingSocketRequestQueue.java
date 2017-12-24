package com.tvd12.ezyfoxserver.socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class EzyPriorityBlockingSocketRequestQueue extends EzyBlockingSocketRequestQueue {

    @Override
    protected BlockingQueue<EzySocketRequest> newQueue(int capacity) {
        return new PriorityBlockingQueue<>(capacity, new EzySocketRequestComparator());
    }

}
