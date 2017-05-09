package com.tvd12.ezyfoxserver.bean.exception;

import java.util.Set;

import com.tvd12.ezyfoxserver.bean.impl.EzyBeanKey;

public class EzySingletonException extends IllegalStateException {
	private static final long serialVersionUID = 814337130118800149L;

	public EzySingletonException(String msg) {
		super(msg);
	}
	
	public static EzySingletonException 
			implementationNotFound(EzyBeanKey key, Set<Class<?>> uncompleted) {
		StringBuilder message = new StringBuilder("bean ")
				.append(key)
				.append(" implementation not found, uncompleted classes: ")
				.append(uncompleted);
		return new EzySingletonException(message.toString());
	}
	
}
