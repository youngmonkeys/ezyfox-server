package com.tvd12.ezyfoxserver.util;

import com.tvd12.ezyfoxserver.function.EzyFoxInitialize;

public class EzyFoxLazyInitializer {

	public <T> T init(Object context, EzyFoxInitialize<T> initer) {
		return init(context, null, initer);
	}
	
	public <T> T init(Object context, T currentValue, EzyFoxInitialize<T> initer) {
		if(currentValue == null) {
			synchronized (context) {
				if(currentValue == null) {
					return initer.init();
				}
			}
		}
		return currentValue;
	}
	
}
