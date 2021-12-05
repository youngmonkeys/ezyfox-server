package com.tvd12.ezyfoxserver.support.asm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.support.exception.EzyDuplicateRequestHandlerException;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestControllerProxy;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestHandlerMethod;

import lombok.Setter;

@SuppressWarnings("rawtypes")
public class EzyRequestHandlersImplementer extends EzyLoggable {
    
    @Setter
    private EzyResponseFactory responseFactory;
	
	public Map<String, EzyUserRequestHandler> implement(Collection<Object> controllers) {
		Map<String, EzyUserRequestHandler> handlers = new HashMap<>();
		for(Object controller : controllers) {
			Map<String, EzyUserRequestHandler> map = implement(controller);
			for(String command : map.keySet()) {
				EzyUserRequestHandler handler = map.get(command);
				EzyUserRequestHandler old = handlers.put(command, handler);
				if(old != null)
					throw new EzyDuplicateRequestHandlerException(command, old, handler);
			}
		}
		return handlers;
	}
	
	public Map<String, EzyUserRequestHandler> implement(Object controller) {
		Map<String, EzyUserRequestHandler> handlers = new HashMap<>();
		EzyRequestControllerProxy proxy = new EzyRequestControllerProxy(controller);
		for(EzyRequestHandlerMethod method : proxy.getRequestHandlerMethods()) {
			EzyRequestHandlerImplementer implementer = newImplementer(proxy, method);
			EzyAsmRequestHandler handler = implementer.implement();
			String command = handler.getCommand();
			EzyUserRequestHandler old = handlers.put(command, handler);
			if(old != null)
				throw new EzyDuplicateRequestHandlerException(command, old, handler);
		}
		return handlers;
	}
	
	protected EzyRequestHandlerImplementer newImplementer(
			EzyRequestControllerProxy controller, EzyRequestHandlerMethod method) {
	    EzyRequestHandlerImplementer implementer = 
	            new EzyRequestHandlerImplementer(controller, method);
	    implementer.setResponseFactory(responseFactory);
	    return implementer;
	}
	
}
