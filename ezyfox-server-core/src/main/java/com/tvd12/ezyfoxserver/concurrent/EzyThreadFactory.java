package com.tvd12.ezyfoxserver.concurrent;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

import io.netty.util.concurrent.DefaultThreadFactory;

public class EzyThreadFactory extends DefaultThreadFactory {

	private static final String EZYFOX_NAME	= "ezyfox";
	
	protected EzyThreadFactory(Builder builder) {
		super(getFullPoolName(builder.getPoolName()), builder.daemon, builder.priority);
	}
	
	protected static String getFullPoolName(String poolName) {
		return EZYFOX_NAME + "-" + poolName;
	}
	
	public static class Builder implements EzyBuilder<EzyThreadFactory> {

		private int priority;
		private boolean daemon;
		private String poolName;
		
		public Builder priority(int priority) {
			this.priority = priority;
			return this;
		}
		public Builder daemon(boolean daemon) {
			this.daemon = daemon;
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
		
		protected String getPoolName() {
			return poolName == null ? "" : poolName.toLowerCase();
		}
		
		@Override
		public EzyThreadFactory build() {
			return new EzyThreadFactory(this);
		}
	}
}
