package com.tvd12.ezyfoxserver.socket;

import lombok.Getter;

@Getter
public class EzySimpleSocketRequestQueues implements EzySocketRequestQueues {

    private final EzySocketRequestQueue systemQueue;
    private final EzySocketRequestQueue extensionQueue;
    
    public EzySimpleSocketRequestQueues() {
        this.systemQueue = new EzyPriorityBlockingSocketRequestQueue();
        this.extensionQueue = new EzyLinkedBlockingSocketRequestQueue();
    }
    
    @Override
    public boolean add(EzySocketRequest request) {
        if(request.isSystemRequest())
            return systemQueue.add(request);
        return extensionQueue.add(request);
    }
    
}
