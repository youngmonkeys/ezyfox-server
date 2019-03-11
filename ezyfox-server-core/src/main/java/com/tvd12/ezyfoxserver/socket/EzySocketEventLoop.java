package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.List;
import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;

public abstract class EzySocketEventLoop 
		extends EzyLoggable
		implements EzyStartable, EzyDestroyable {

	protected ExecutorService threadPool;
	
	protected volatile boolean active;
	
	public EzySocketEventLoop() {
	}
	
	protected abstract String threadName();
	protected abstract int threadPoolSize();
	
	@Override
	public void start() throws Exception {
	    initThreadPool();
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
	
	protected void initThreadPool() {
	    this.threadPool = EzyExecutors.newFixedThreadPool(threadPoolSize(), threadName());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> threadPool.shutdown()));
	}
	
	@Override
	public void destroy() {
		processWithLogException(this::destroy0);
	}
	
	protected void destroy0() throws Exception {
		setActive(false);
		if(threadPool != null) {
		    List<Runnable> remainTasks = threadPool.shutdownNow();
		    logger.debug("{} has stopped. Never commenced execution task: {}", getClass().getSimpleName(), remainTasks.size());
		}
	}
	
}
