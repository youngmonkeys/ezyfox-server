package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Setter;

public abstract class EzySocketRequestHandler extends EzySocketAbstractEventHandler {

    @Setter
    protected EzySessionTicketsQueue sessionTicketsQueue;
    @Setter
    protected EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher;

    @Override
    public void handleEvent() {
        processRequestQueue0();
    }

    @Override
    public void destroy() {
        processWithLogException(() -> sessionTicketsQueue.clear());
    }

    private void processRequestQueue0() {
        EzySocketRequest request = null;
        try {
            EzySession session = sessionTicketsQueue.take();
            EzyRequestQueue requestQueue = getRequestQueue(session);
            synchronized (requestQueue) {
                request = requestQueue.take();
                if(requestQueue.size() > 0)
                    sessionTicketsQueue.add(session);
            }
            processRequestQueue(request);
        }
        catch (InterruptedException e) {
            logger.info("{}-request-handler thread interrupted: {}", getRequestType(), Thread.currentThread());
        }
        catch(Throwable throwable) {
            logger.warn("problems in {}-request-handler, thread: {}", getRequestType(), Thread.currentThread(), throwable);
        }
        finally {
            if(request != null)
                request.release();
        }
    }

    protected abstract EzyRequestQueue getRequestQueue(EzySession session);

    protected abstract String getRequestType();

    private void processRequestQueue(EzySocketRequest request) throws Exception {
        try {
            processRequestQueue0(request);
        }
        finally {
            request.release();
        }
    }

    private void processRequestQueue0(EzySocketRequest request) throws Exception {
        EzyArray data = request.getData();
        EzySession session = request.getSession();
        EzySocketDataHandlerGroup handlerGroup = getDataHandlerGroup(session);
        if(handlerGroup != null)
            handlerGroup.fireChannelRead(request.getCommand(), data);
        else
            logger.warn("has no handler group with session: {}, drop request: {}", session, request);
    }

    protected EzySocketDataHandlerGroup getDataHandlerGroup(EzySession session) {
        return dataHandlerGroupFetcher.getDataHandlerGroup(session);
    }

}
