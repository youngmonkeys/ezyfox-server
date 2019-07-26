package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Setter;

public abstract class EzySocketRequestHandler 
        extends EzySocketAbstractEventHandler
        implements EzySocketRequestQueueAware {

    @Setter
	protected EzySocketRequestQueue requestQueue;
    @Setter
    protected EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher;
	
	@Override
    public void handleEvent() {
	    processRequestQueue0();
	}
	
	@Override
	public void destroy() {
	    processWithLogException(() -> requestQueue.clear());
	}
	
	private void processRequestQueue0() {
		try {
			EzySocketRequest request = requestQueue.take();
			processRequestQueue(request);
		} 
		catch (InterruptedException e) {
			logger.warn("{}-request-handler thread interrupted: {}", getRequestType(), Thread.currentThread());
		}
		catch(Throwable throwable) {
			logger.warn("problems in {}-request-handler, thread: {}", getRequestType(), Thread.currentThread(), throwable);
		}
	}
	
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
