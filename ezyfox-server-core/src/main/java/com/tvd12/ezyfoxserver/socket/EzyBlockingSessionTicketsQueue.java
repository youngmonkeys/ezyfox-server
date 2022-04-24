package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzySession;

import java.util.concurrent.LinkedBlockingQueue;

public class EzyBlockingSessionTicketsQueue
    extends EzyLoggable
    implements EzySessionTicketsQueue {

    private final LinkedBlockingQueue<EzySession> queue;

    public EzyBlockingSessionTicketsQueue() {
        this.queue = new LinkedBlockingQueue<>();
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
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean add(EzySession session) {
        boolean result = queue.offer(session);
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EzySession> T take() throws InterruptedException {
        while (true) {
            T session = (T) queue.take();
            if (session.isActivated()) {
                return session;
            }
            logger.debug("session: {} maybe destroyed", session);
        }
    }
}
