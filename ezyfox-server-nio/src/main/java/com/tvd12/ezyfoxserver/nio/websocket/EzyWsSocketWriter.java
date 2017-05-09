package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.nio.socket.EzyPacket;
import com.tvd12.ezyfoxserver.nio.socket.EzyPacketQueue;
import com.tvd12.ezyfoxserver.nio.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketHandler;

public class EzyWsSocketWriter extends EzySocketHandler {

	private final EzySessionTicketsQueue sessionTicketsQueue;
	
	public EzyWsSocketWriter(Builder builder) {
		super(builder);
		this.sessionTicketsQueue = builder.sessionTicketsQueue;
	}
	
	@Override
	protected String getThreadName() {
		return "ws-socket-writer";
	}
	
	@Override
	protected void tryDestroy() throws Exception {
		super.tryDestroy();
		this.sessionTicketsQueue.clear();
	}
	
	@Override
	protected void tryLoop() {
		while(active) {
			tryProcessSessionTicketsQueue();
		}
		getLogger().info("ws-socket-writer threadpool shutting down.");
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
			getLogger().warn("problems in ws-socket-writer main loop, thread: " + Thread.currentThread(), throwable);
		}
	}
	
	private void processSessionQueue(EzyNioSession session) throws Exception {
		Object connection = session.getConnection();
		if(connection == null) return;
		EzyHandlerGroup group = handlerGroupManager.getHandlerGroup(connection);
		if(group == null) return;
		EzyPacketQueue queue = session.getPacketQueue();
		boolean emptyQueue = processSessionQueue(group, queue);
		if(!emptyQueue) sessionTicketsQueue.add(session);
	}
	
	private boolean processSessionQueue(EzyHandlerGroup group, EzyPacketQueue queue)
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
		public EzyWsSocketWriter build() {
			return new EzyWsSocketWriter(this);
		}
		
	}
}
