package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Setter;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzySocketWriter
    extends EzySocketAbstractEventHandler
    implements EzySessionTicketsQueueAware {

    @Setter
    protected EzySessionTicketsQueue sessionTicketsQueue;
    @Setter
    protected EzySocketWriterGroupFetcher writerGroupFetcher;

    @Override
    public void handleEvent() {
        try {
            EzySession session = sessionTicketsQueue.take();
            processSessionQueue(session);
        } catch (InterruptedException e) {
            logger.info("socket-writer thread interrupted");
        } catch (Throwable e) {
            logger.info("problems in socket-writer, thread", e);
        }
    }

    @Override
    public void destroy() {
        processWithLogException(() -> sessionTicketsQueue.clear());
    }

    private void processSessionQueue(
        EzySession session
    ) throws Exception {
        EzySocketWriterGroup group = writerGroupFetcher
            .getWriterGroup(session);
        if (group == null) {
            return;
        }
        EzyPacketQueue queue = session.getPacketQueue();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (queue) {
            boolean emptyQueue = processSessionQueue(group, queue);
            if (!emptyQueue) {
                sessionTicketsQueue.add(session);
            }
        }
    }

    private boolean processSessionQueue(
        EzySocketWriterGroup group,
        EzyPacketQueue queue
    )
        throws Exception {
        if (!queue.isEmpty()) {
            EzyPacket packet = queue.peek();
            Object writeBuffer = getWriteBuffer();
            group.firePacketSend(packet, writeBuffer);
            if (packet.isReleased()) {
                queue.take();
            }
            return queue.isEmpty();
        }
        return true;
    }

    protected Object getWriteBuffer() {
        return null;
    }
}
