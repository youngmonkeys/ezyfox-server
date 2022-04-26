package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Setter;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzySocketStreamHandler extends EzySocketAbstractEventHandler {

    @Setter
    protected EzySocketStreamQueue streamQueue;
    @Setter
    protected EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher;

    @Override
    public void handleEvent() {
        try {
            EzySocketStream stream = streamQueue.take();
            processStream(stream);
        } catch (InterruptedException e) {
            logger.warn("socket-stream-handler thread interrupted");
        } catch (Throwable throwable) {
            logger.warn("problems in socket-stream-handler", throwable);
        }
    }

    @Override
    public void destroy() {
        processWithLogException(streamQueue::clear);
    }

    private void processStream(EzySocketStream stream) throws Exception {
        try {
            byte[] bytes = stream.getBytes();
            EzySession session = stream.getSession();
            EzySocketDataHandlerGroup handlerGroup =
                getDataHandlerGroup(session);
            if (handlerGroup != null) {
                handlerGroup.fireStreamBytesReceived(bytes);
            } else {
                logger.warn(
                    "has no handler group with session: {}, drop: {} bytes",
                    session,
                    bytes.length
                );
            }
        } finally {
            stream.release();
        }
    }

    protected EzySocketDataHandlerGroup getDataHandlerGroup(
        EzySession session
    ) {
        return dataHandlerGroupFetcher.getDataHandlerGroup(session);
    }
}
