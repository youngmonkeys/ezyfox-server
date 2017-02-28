package com.tvd12.ezyfoxserver.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class EzyExecutors {

	private EzyExecutors() {
	}
	
	public static ScheduledExecutorService newScheduledThreadPool(
			final int corePoolSize, final String threadName) {
		return Executors.newScheduledThreadPool(corePoolSize, newThreadFactory(threadName));
	}
	
	public static ExecutorService newFixedThreadPool(
			final int nThreads, final String threadName) {
		return Executors.newFixedThreadPool(nThreads, newThreadFactory(threadName));
	}
	
	public static ExecutorService newSingleThreadExecutor(
			final String threadName) {
		return Executors.newSingleThreadExecutor(newThreadFactory(threadName));
	}
	
	public static ScheduledExecutorService newSingleThreadScheduledExecutor(
			final String threadName) {
		return Executors.newSingleThreadScheduledExecutor(newThreadFactory(threadName));
	}
	
	public static EzyThreadFactory newThreadFactory(final String poolName) {
		return newThreadFactory(poolName, false, Thread.NORM_PRIORITY);
	}
	
	public static EzyThreadFactory newThreadFactory(
			final String poolName, final int priority) {
		return newThreadFactory(poolName, false, priority);
	}
	
	public static EzyThreadFactory newThreadFactory(
			final String poolName, final boolean daemon) {
		return newThreadFactory(poolName, daemon, Thread.NORM_PRIORITY);
	}
	
	public static EzyThreadFactory newThreadFactory(
			final String poolName, final boolean daemon, final int priority) {
		return new EzyThreadFactory.Builder()
				.daemon(daemon)
				.priority(priority)
				.poolName(poolName)
				.build();
	}
}
