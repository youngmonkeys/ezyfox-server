package com.tvd12.ezyfoxserver.nio.socket;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.nio.channels.SocketChannel;

import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzyPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;

public class EzySocketWriter extends EzySocketHandler {

	private final EzySessionTicketsQueue sessionTicketsQueue;
	
	public EzySocketWriter(Builder builder) {
		super(builder);
		this.sessionTicketsQueue = builder.sessionTicketsQueue;
	}
	
	@Override
	protected String getThreadName() {
		return "socket-writer";
	}
	
	@Override
	protected void tryDestroy() throws Exception {
		super.tryDestroy();
		processWithLogException(sessionTicketsQueue::clear);
	}
	
	@Override
	protected void tryLoop() {
		while(active) {
			tryProcessSessionTicketsQueue();
		}
		getLogger().info("socket-writer threadpool shutting down.");
	}
	
	private void tryProcessSessionTicketsQueue() {
		try {
			EzyNioSession session = sessionTicketsQueue.take();
			processSessionQueue(session);
		} 
		catch (InterruptedException e) {
			getLogger().warn("socket-writer thread interrupted: " + Thread.currentThread());
			setActive(false);
		}
		catch(Throwable throwable) {
			getLogger().warn("problems in socket-writer main loop, thread: " + Thread.currentThread(), throwable);
		}
	}
	
	private void processSessionQueue(EzyNioSession session) throws Exception {
		SocketChannel channel = session.getConnection();
		if(channel == null) return;
		EzyNioHandlerGroup group = handlerGroupManager.getHandlerGroup(channel);
		if(group == null) return;
		EzyPacketQueue queue = session.getPacketQueue();
		boolean emptyQueue = processSessionQueue(group, queue);
		if(!emptyQueue) sessionTicketsQueue.add(session);
	}
	
	private boolean processSessionQueue(EzyNioHandlerGroup group, EzyPacketQueue queue)
			throws Exception {
		if(!queue.isEmpty()) {
			EzyPacket packet = queue.take();
			group.fireDataSend(packet.getData());
			return queue.isEmpty();
		}
		return true;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySocketHandler.Builder<Builder> {

		private EzySessionTicketsQueue sessionTicketsQueue;
		
		public Builder sessionTicketsQueue(EzySessionTicketsQueue queue) {
			this.sessionTicketsQueue = queue;
			return this;
		}
		
		@Override
		public EzySocketWriter build() {
			return new EzySocketWriter(this);
		}
		
	}
}
