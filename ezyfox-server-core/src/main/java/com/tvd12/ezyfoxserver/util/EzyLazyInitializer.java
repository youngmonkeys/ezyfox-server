package com.tvd12.ezyfoxserver.util;

import com.tvd12.ezyfoxserver.function.EzyInitialize;

public class EzyLazyInitializer {

	public <T> T init(Object context, EzyInitialize<T> initer) {
		return init(context, null, initer);
	}
	
	public <T> T init(Object context, T currentValue, EzyInitialize<T> initer) {
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
