package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Getter;

@Getter
public class EzySessionTicketsRequestQueues {

    private final EzySessionTicketsQueue systemQueue;
    private final EzySessionTicketsQueue extensionQueue;

    {
        systemQueue = new EzyBlockingSessionTicketsQueue();
        extensionQueue = new EzyBlockingSessionTicketsQueue();
    }

    public boolean addRequest(EzySocketRequest request) {
        EzyRequestQueue queue;
        EzySessionTicketsQueue ticketsQueue;
        EzySession session = request.getSession();
        if(request.isSystemRequest()) {
            ticketsQueue = systemQueue;
            queue = session.getSystemRequestQueue();
        }
        else {
            ticketsQueue = extensionQueue;
            queue = session.getExtensionRequestQueue();
        }
        boolean success;
        synchronized (queue) {
            boolean empty = queue.isEmpty();
            success = queue.add(request);
            if(empty && success)
                ticketsQueue.add(session);
        }
        return success;
    }
}
