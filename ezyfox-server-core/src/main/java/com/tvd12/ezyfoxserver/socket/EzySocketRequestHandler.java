package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfoxserver.entity.EzyArray;

import lombok.Setter;

public abstract class EzySocketRequestHandler 
        extends EzySocketAbstractEventHandler
        implements EzySocketRequestQueueAware {

    @Setter
	protected EzySocketRequestQueue requestQueue;
	
	@Override
    public void handleEvent() {
	    processRequestQueue0();
	}
	
	@Override
	public void destroy() {
	    processWithLogException(requestQueue::clear);
	}
	
	private void processRequestQueue0() {
		try {
			EzySocketRequest request = requestQueue.take();
			processRequestQueue(request);
		} 
		catch (InterruptedException e) {
			getLogger().warn(getRequestType() + "socket-request-handler thread interrupted: " + Thread.currentThread());
		}
		catch(Throwable throwable) {
			getLogger().warn("problems in " + getRequestType() + "-socket-request-handler main loop, thread: " + Thread.currentThread(), throwable);
		}
	}
	
	protected abstract String getRequestType();
	
	private void processRequestQueue(EzySocketRequest request) throws Exception {
	    EzyArray data = request.getData();
	    EzySocketDataHandler handler = request.getHandler();
	    handler.channelRead(request.getCommand(), data);
	    request.release();
	}
	
}
