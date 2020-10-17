package com.tvd12.ezyfoxserver.support.asm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.support.handler.EzyUncaughtExceptionHandler;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerMethod;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerProxy;

@SuppressWarnings("rawtypes")
public class EzyExceptionHandlersImplementer extends EzyLoggable {
	
	public Map<Class<?>, EzyUncaughtExceptionHandler> 
			implement(Collection<Object> exceptionHandlers) {
		Map<Class<?>, EzyUncaughtExceptionHandler> handlers = new HashMap<>();
		for(Object controller : exceptionHandlers)
			handlers.putAll(implement(controller));
		return handlers;
	}
	
	public Map<Class<?>, EzyUncaughtExceptionHandler> implement(Object exceptionHandler) {
		Map<Class<?>, EzyUncaughtExceptionHandler> handlers = new HashMap<>();
		EzyExceptionHandlerProxy proxy = new EzyExceptionHandlerProxy(exceptionHandler);
		for(EzyExceptionHandlerMethod method : proxy.getExceptionHandlerMethods()) {
			EzyExceptionHandlerImplementer implementer = newImplementer(proxy, method);
			EzyUncaughtExceptionHandler handler = implementer.implement();
			for(Class<?> exceptionClass : method.getExceptionClasses())
				handlers.put(exceptionClass, handler);
		}
		return handlers;
	}
	
	protected EzyExceptionHandlerImplementer newImplementer(
			EzyExceptionHandlerProxy exceptionHandler, EzyExceptionHandlerMethod method) {
		return new EzyExceptionHandlerImplementer(exceptionHandler, method);
	}
	
}
