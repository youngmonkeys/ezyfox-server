package com.tvd12.ezyfoxserver.concurrent;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import com.tvd12.ezyfoxserver.function.EzyInitialize;

public class EzyLazyInitializer<T> extends LazyInitializer<T> {

	private EzyInitialize<T> initializer;
	
	public EzyLazyInitializer(EzyInitialize<T> initializer) {
		this.initializer = initializer;
	}
	
	@Override
	protected T initialize() throws ConcurrentException {
		return initializer.init();
	}
	
	@Override
	public T get() {
		try {
			return doGet();
		} catch (ConcurrentException e) {
			throw new IllegalStateException(e);
		}
	}
	
	protected T doGet() throws ConcurrentException {
		return super.get();
	}
	
}
