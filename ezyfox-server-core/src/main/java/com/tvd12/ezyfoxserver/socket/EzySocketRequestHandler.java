package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Setter;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public abstract class EzySocketRequestHandler extends EzySocketAbstractEventHandler {

    @Setter
    protected EzySessionTicketsQueue sessionTicketsQueue;
    @Setter
    protected EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher;

    @Override
    public void handleEvent() {
        EzySocketRequest request = null;
        try {
            EzySession session = sessionTicketsQueue.take();
            EzyRequestQueue requestQueue = getRequestQueue(session);

            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (requestQueue) {
                request = requestQueue.take();
                if (requestQueue.size() > 0) {
                    sessionTicketsQueue.add(session);
                }
            }
            processRequest(request);
        } catch (InterruptedException e) {
            logger.info(
                "{}-request-handler thread interrupted",
                getRequestType()
            );
        } catch (Throwable throwable) {
            logger.warn(
                "problems in {}-request-handler",
                getRequestType(),
                throwable
            );
        } finally {
            if (request != null) {
                request.release();
            }
        }
    }

    @Override
    public void destroy() {
        processWithLogException(() -> sessionTicketsQueue.clear());
    }

    protected abstract EzyRequestQueue getRequestQueue(
        EzySession session
    );

    protected abstract String getRequestType();

    private void processRequest(
        EzySocketRequest request
    ) throws Exception {
        try {
            EzyArray data = request.getData();
            EzySession session = request.getSession();
            EzySocketDataHandlerGroup handlerGroup =
                getDataHandlerGroup(session);
            if (handlerGroup != null) {
                handlerGroup.fireChannelRead(request.getCommand(), data);
            } else {
                logger.warn(
                    "has no handler group with session: {}, drop request: {}",
                    session,
                    request
                );
            }
        } finally {
            request.release();
        }
    }

    protected EzySocketDataHandlerGroup getDataHandlerGroup(
        EzySession session
    ) {
        return dataHandlerGroupFetcher.getDataHandlerGroup(session);
    }
}
