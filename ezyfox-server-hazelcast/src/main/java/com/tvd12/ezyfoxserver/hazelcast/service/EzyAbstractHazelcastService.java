package com.tvd12.ezyfoxserver.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.ILock;
import com.tvd12.ezyfoxserver.function.EzyExceptionVoid;
import com.tvd12.ezyfoxserver.function.EzySupplier;
import com.tvd12.ezyfoxserver.util.EzyInitable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public abstract class EzyAbstractHazelcastService 
		extends EzyLoggable
		implements HazelcastInstanceAware, EzyInitable {

	protected HazelcastInstance hazelcastInstance;
	
	public EzyAbstractHazelcastService() {
	}
	
	public EzyAbstractHazelcastService(HazelcastInstance hazelcastInstance) {
		this.setHazelcastInstance(hazelcastInstance);
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}
	
	protected ILock getLock(String key) {
		return hazelcastInstance.getLock(key);
	}
	
	protected void lockUpdate(String key, EzyExceptionVoid applier) {
		ILock lock = getLock(key);
		lock.lock();
		try {
			applier.apply();
		}
		catch (Exception e) {
			throw new IllegalStateException("lock to update with key " + key + " error", e);
		}
		finally {
			lock.unlock();
		}
	}
	
	protected <T> T lockUpdateAndGet(String key, EzySupplier<T> supplier) {
		ILock lock = getLock(key);
		lock.lock();
		try {
			return supplier.get();
		}
		catch (Exception e) {
			throw new IllegalStateException("lock to update and get with key " + key + " error", e);
		}
		finally {
			lock.unlock();
		}
	}
	
	protected void lockUpdateWithException(
			String key, EzyExceptionVoid applier) throws Exception {
		ILock lock = getLock(key);
		lock.lock();
		try {
			applier.apply();
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			lock.unlock();
		}
	}
	
	protected <T> T lockUpdateAndGetWithException(
			String key, EzySupplier<T> supplier) throws Exception {
		ILock lock = getLock(key);
		lock.lock();
		try {
			return supplier.get();
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			lock.unlock();
		}
	}
	
}
