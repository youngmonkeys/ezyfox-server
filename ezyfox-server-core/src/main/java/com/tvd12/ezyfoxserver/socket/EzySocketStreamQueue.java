package com.tvd12.ezyfoxserver.socket;

public interface EzySocketStreamQueue {

    int size();

    void clear();

    boolean isFull();

    boolean isEmpty();

    boolean add(EzySocketStream stream);

    void remove(EzySocketStream stream);

    EzySocketStream take() throws InterruptedException;

}
