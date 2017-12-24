package com.tvd12.ezyfoxserver.socket;

public interface EzySocketRequestQueues {

    boolean add(EzySocketRequest request);
    
    EzySocketRequestQueue getSystemQueue();
    
    EzySocketRequestQueue getExtensionQueue();
    
}
