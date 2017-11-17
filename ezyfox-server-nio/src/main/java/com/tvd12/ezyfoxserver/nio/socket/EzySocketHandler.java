package com.tvd12.ezyfoxserver.nio.socket;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.util.List;
import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

public abstract class EzySocketHandler 
		extends EzyLoggable
		implements EzyStartable, EzyDestroyable {

	protected int threadPoolSize;
	protected ExecutorService threadPool;
	protected EzyHandlerGroupManager handlerGroupManager;
	
	protected volatile boolean active;
	
	public EzySocketHandler(Builder<?> builder) {
		this.threadPoolSize = builder.threadPoolSize;
		this.handlerGroupManager = builder.handlerGroupManager;
		this.threadPool = EzyExecutors.newFixedThreadPool(threadPoolSize, "socket-acceptor");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> threadPool.shutdown()));
	}
	
	protected abstract String getThreadName();
	
	@Override
	public void start() throws Exception {
		setActive(true);
		startLoopService();
	}
	
	protected void setActive(boolean value) {
		this.active = value;
	}
	
	private void startLoopService() {
		Runnable task = newServiceTask();
		for(int i = 0 ; i < threadPoolSize ; i++)
			threadPool.execute(task);
	}
	
	private Runnable newServiceTask() {
		return this::tryLoop;
	}
	
	protected abstract void tryLoop();
	
	@Override
	public void destroy() {
		processWithLogException(this::tryDestroy);
	}
	
	protected void tryDestroy() throws Exception {
		setActive(false);
		List<Runnable> remainTasks = threadPool.shutdownNow();
		getLogger().info("{} stopped. Never commenced execution task: " + remainTasks.size());
	}
	
	public static abstract class Builder<B extends Builder<B>> 
			implements EzyBuilder<EzySocketHandler> {
		protected int threadPoolSize;
		protected EzyHandlerGroupManager handlerGroupManager;
		
		@SuppressWarnings("unchecked")
		public B threadPoolSize(int threadPoolSize) {
			this.threadPoolSize = threadPoolSize;
			return (B)this;
		}
		
		@SuppressWarnings("unchecked")
		public B handlerGroupManager(EzyHandlerGroupManager manager) {
			this.handlerGroupManager = manager;
			return (B)this;
		}
	}
	
}
