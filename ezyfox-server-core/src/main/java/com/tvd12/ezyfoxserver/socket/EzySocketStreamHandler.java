package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Setter;

public class EzySocketStreamHandler extends EzySocketAbstractEventHandler {

    @Setter
	protected EzySocketStreamQueue streamQueue;
    @Setter
    protected EzySocketDataHandlerGroupFetcher dataHandlerGroupFetcher;
	
	@Override
    public void handleEvent() {
	    processStreamQueue0();
	}
	
	@Override
	public void destroy() {
	    processWithLogException(streamQueue::clear);
	}
	
	private void processStreamQueue0() {
		try {
			EzySocketStream stream = streamQueue.take();
			processStreamQueue(stream);
		} 
		catch (InterruptedException e) {
			logger.warn("socket-stream-handler thread interrupted: " + Thread.currentThread());
		}
		catch(Throwable throwable) {
			logger.warn("problems in socket-stream-handler main loop, thread: " + Thread.currentThread(), throwable);
		}
	}
	
	private void processStreamQueue(EzySocketStream stream) throws Exception {
	    try {
	        processStreamQueue0(stream);
	    }
	    finally {
	        stream.release();
	    }
	}
	
	private void processStreamQueue0(EzySocketStream stream) throws Exception {
	    byte[] bytes = stream.getBytes();
	    EzySession session = stream.getSession();
	    EzySocketDataHandlerGroup handlerGroup = getDataHandlerGroup(session);
	    if(handlerGroup != null)
	        handlerGroup.fireStreamBytesReceived(bytes);
	    else 
	        logger.warn("has no handler group with session: " + session + ", drop " + bytes.length + " bytes");
	}
	
	protected EzySocketDataHandlerGroup getDataHandlerGroup(EzySession session) {
	    return dataHandlerGroupFetcher.getDataHandlerGroup(session);
	}
	
}
