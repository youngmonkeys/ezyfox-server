package com.tvd12.ezyfoxserver.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class EzyExecutors {

	private EzyExecutors() {
	}
	
	public static ScheduledExecutorService newScheduledThreadPool(
			int corePoolSize, String threadName) {
		return newScheduledThreadPool(corePoolSize, newThreadFactory(threadName));
	}
	
	public static ExecutorService newFixedThreadPool(
			int nThreads, String threadName) {
		return newFixedThreadPool(nThreads, newThreadFactory(threadName));
	}
	
	public static ExecutorService newSingleThreadExecutor(
			String threadName) {
		return newSingleThreadExecutor(newThreadFactory(threadName));
	}
	
	public static ScheduledExecutorService newSingleThreadScheduledExecutor(
			String threadName) {
		return newSingleThreadScheduledExecutor(newThreadFactory(threadName));
	}
	
	// ====== by thread factory ==========
	public static ScheduledExecutorService newScheduledThreadPool(
			int corePoolSize, ThreadFactory threadFactory) {
		return Executors.newScheduledThreadPool(corePoolSize, threadFactory);
	}
	
	public static ExecutorService newFixedThreadPool(
			int nThreads, ThreadFactory threadFactory) {
		return Executors.newFixedThreadPool(nThreads, threadFactory);
	}
	
	public static ExecutorService newSingleThreadExecutor(
			ThreadFactory threadFactory) {
		return Executors.newSingleThreadExecutor(threadFactory);
	}
	
	public static ScheduledExecutorService newSingleThreadScheduledExecutor(
			ThreadFactory threadFactory) {
		return Executors.newSingleThreadScheduledExecutor(threadFactory);
	}
	
	public static ScheduledExecutorService newErrorScheduledExecutor(
			String errorMessage) {
		return new EzyErrorScheduledExecutorService(errorMessage);
	}
	
	// ===================================
	
	public static EzyThreadFactory newThreadFactory(String poolName) {
		return newThreadFactory(poolName, false, Thread.NORM_PRIORITY);
	}
	
	public static EzyThreadFactory newThreadFactory(
			String poolName, int priority) {
		return newThreadFactory(poolName, false, priority);
	}
	
	public static EzyThreadFactory newThreadFactory(
			String poolName, boolean daemon) {
		return newThreadFactory(poolName, daemon, Thread.NORM_PRIORITY);
	}
	
	public static EzyThreadFactory newThreadFactory(
			String poolName, boolean daemon, int priority) {
		return EzyThreadFactory.builder()
				.daemon(daemon)
				.priority(priority)
				.poolName(poolName)
				.build();
	}
}
