package com.tvd12.ezyfoxserver.hazelcast.transaction;

import java.util.concurrent.TimeUnit;

public abstract class EzyTransactionOptions {

	public static EzySimpleTransactionOptions newInstance() {
		return new EzySimpleTransactionOptions();
	}
	
	public abstract long getTimeout();
	
	public abstract TimeUnit getTimeoutUnit();
	
	public abstract int getDurability();
	
	public abstract EzyTransactionType getTransactionType();
	
}
