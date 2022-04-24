package com.tvd12.ezyfoxserver.socket;

public interface EzySocketUserRemovalQueue {

    int size();

    void clear();

    boolean isEmpty();

    boolean add(EzySocketUserRemoval removal);

    void remove(EzySocketUserRemoval removal);

    EzySocketUserRemoval take() throws InterruptedException;

}
