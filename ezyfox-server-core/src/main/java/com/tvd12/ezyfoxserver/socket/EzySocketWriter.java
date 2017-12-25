package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Setter;

public abstract class EzySocketWriter 
        extends EzySocketAbstractEventHandler
        implements EzySessionTicketsQueueAware {

    @Setter
	protected EzySessionTicketsQueue sessionTicketsQueue;
	
	@Override
    public void handleEvent() {
	    processSessionTicketsQueue0();
	}
	
	@Override
	public void destroy() {
	    processWithLogException(sessionTicketsQueue::clear);
	}
	
	private void processSessionTicketsQueue0() {
		try {
			EzySession session = sessionTicketsQueue.take();
			processSessionQueue(session);
		} 
		catch (InterruptedException e) {
			getLogger().warn("socket-writer thread interrupted: " + Thread.currentThread());
		}
		catch(Throwable throwable) {
			getLogger().warn("problems in socket-writer main loop, thread: " + Thread.currentThread(), throwable);
		}
	}
	
	private void processSessionQueue(EzySession session) throws Exception {
	    EzySocketWriterGroup group = getWriterGroup(session);
		if(group == null) return;
		EzyPacketQueue queue = session.getPacketQueue();
		synchronized (queue) {
		    boolean emptyQueue = processSessionQueue(group, queue);
	        if(!emptyQueue) { 
	            sessionTicketsQueue.add(session);
	        }
        }
	}
	
	protected abstract EzySocketWriterGroup getWriterGroup(EzySession session);
	
	private boolean processSessionQueue(EzySocketWriterGroup group, EzyPacketQueue queue)
			throws Exception {
		if(!queue.isEmpty()) {
			EzyPacket packet = queue.peek();
			group.firePacketSend(packet);
	        if(packet.isReleased())
	            queue.take();
			return queue.isEmpty();
		}
		return true;
	}
	
}
