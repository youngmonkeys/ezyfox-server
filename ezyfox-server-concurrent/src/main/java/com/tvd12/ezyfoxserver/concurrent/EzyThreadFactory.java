package com.tvd12.ezyfoxserver.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyThreadFactory extends EzyLoggable implements ThreadFactory  {

	private int poolId;
	protected int priority;
	protected String prefix;
	protected boolean daemon;
	protected String poolName;
	protected String threadPrefix;
	protected ThreadGroup threadGroup;
	protected AtomicInteger threadCounter = new AtomicInteger();
	
	private static final AtomicInteger POOL_COUNTER = new AtomicInteger();
	
	protected EzyThreadFactory(Builder builder) {
		this.poolId = POOL_COUNTER.incrementAndGet();
		this.prefix = builder.prefix;
		this.daemon = builder.daemon;
		this.priority = builder.priority;
		this.poolName = builder.getPoolName();
		this.threadGroup = builder.threadGroup;
		this.poolName = getFullPoolName();
		this.threadPrefix = poolName + '-' + poolId + '-';
	}
	
	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = createThread(runnable, getThreadName());
		setUpThread(thread);
		return thread;
	}
	
	protected Thread createThread(Runnable runnable, String name) {
		return new Thread(runnable, name);
	}
	
	protected void setUpThread(Thread thread) {
		try {
			trySetUpThread(thread);
		}
		catch(Exception e) {
			getLogger().warn("can not setup thread " + thread.getName(), e);
		}
	}
	
	protected void trySetUpThread(Thread thread) {
		if (thread.isDaemon()) {
            if (!daemon)
            	thread.setDaemon(false);
        } else {
            if (daemon)
            	thread.setDaemon(true);
        }
        if (thread.getPriority() != priority)
        	thread.setPriority(priority);
	}
	
	protected String getThreadName() {
		return threadPrefix + threadCounter.incrementAndGet();
	}
	
	protected String getFullPoolName() {
		return StringUtils.isEmpty(prefix) ? poolName : prefix + "-" + poolName;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyThreadFactory> {

		protected int priority;
		protected String prefix;
		protected boolean daemon;
		protected String poolName;
		protected ThreadGroup threadGroup;
		
		public Builder() {
			this.poolName = "";
			this.daemon = false;
			this.prefix = "ezyfox";
			this.priority = Thread.NORM_PRIORITY;
			this.threadGroup = getSystemThreadGroup();
		}
		
		public Builder priority(int priority) {
			validatePriority(priority);
			this.priority = priority;
			return this;
		}
		
		public Builder daemon(boolean daemon) {
			this.daemon = daemon;
			return this;
		}
		
		public Builder prefix(String prefix) {
			this.prefix = prefix;
			return this;
		}
		
		public Builder poolName(String poolName) {
			this.poolName = poolName;
			return this;
		}
		
		@SuppressWarnings("rawtypes")
		public Builder poolName(Class poolType) {
			this.poolName = poolType.getSimpleName();
			return this;
		}
		
		public Builder threadGroup(ThreadGroup threadGroup) {
			this.threadGroup = threadGroup;
			return this;
		}
		
		protected String getPoolName() {
			return poolName == null ? "" : poolName.toLowerCase();
		}
		
		@Override
		public EzyThreadFactory build() {
			return new EzyThreadFactory(this);
		}
		
		protected void validatePriority(int priority) {
			 if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) 
		            throw new IllegalArgumentException(
		                    "priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
		}
		
		protected ThreadGroup getSystemThreadGroup() {
	    	return getSecurityManager() == null 
	    			? Thread.currentThread().getThreadGroup() 
	    			: getSecurityManager().getThreadGroup();
	    }
		
		protected SecurityManager getSecurityManager() {
			return System.getSecurityManager();
		}
		
	}
	
}
