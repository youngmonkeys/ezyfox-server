package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.util.List;
import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

public abstract class EzySocketEventLoop 
		extends EzyLoggable
		implements EzyStartable, EzyDestroyable {

	protected ExecutorService threadPool;
	
	protected volatile boolean active;
	
	public EzySocketEventLoop() {
		this.threadPool = EzyExecutors.newFixedThreadPool(threadPoolSize(), threadName());
		Runtime.getRuntime().addShutdownHook(new Thread(() -> threadPool.shutdown()));
	}
	
	protected abstract String threadName();
	protected abstract int threadPoolSize();
	
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
		int threadPoolSize = threadPoolSize();
		for(int i = 0 ; i < threadPoolSize ; i++)
			threadPool.execute(task);
	}
	
	private Runnable newServiceTask() {
		return this::eventLoop;
	}
	
	protected abstract void eventLoop();
	
	@Override
	public void destroy() {
		processWithLogException(this::destroy0);
	}
	
	protected void destroy0() throws Exception {
		setActive(false);
		List<Runnable> remainTasks = threadPool.shutdownNow();
		getLogger().info("{} stopped. Never commenced execution task: " + remainTasks.size());
	}
	
}
