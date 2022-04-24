package com.tvd12.ezyfoxserver.socket;

public interface EzyRequestQueue {

    int size();

    void clear();

    EzySocketRequest take();

    boolean isFull();

    boolean isEmpty();

    boolean add(EzySocketRequest request);

}
