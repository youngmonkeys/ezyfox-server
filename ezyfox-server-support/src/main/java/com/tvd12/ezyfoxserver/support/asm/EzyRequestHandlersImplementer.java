package com.tvd12.ezyfoxserver.support.asm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.support.exception.EzyDuplicateRequestHandlerException;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.manager.EzyFeatureCommandManager;
import com.tvd12.ezyfoxserver.support.manager.EzyRequestCommandManager;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestControllerProxy;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestHandlerMethod;

import lombok.Setter;

@Setter
@SuppressWarnings("rawtypes")
public class EzyRequestHandlersImplementer extends EzyLoggable {
    
    private EzyResponseFactory responseFactory;
    private EzyFeatureCommandManager featureCommandManager; 
    private EzyRequestCommandManager requestCommandManager;
	
	public Map<String, EzyUserRequestHandler> implement(Collection<Object> controllers) {
		Map<String, EzyUserRequestHandler> handlers = new HashMap<>();
		for(Object controller : controllers) {
			Map<String, EzyUserRequestHandler> map = implement(controller);
			for(String command : map.keySet()) {
				EzyUserRequestHandler handler = map.get(command);
				EzyUserRequestHandler old = handlers.put(command, handler);
				if(old != null) {
					throw new EzyDuplicateRequestHandlerException(command, old, handler);
				}
			}
		}
		return handlers;
	}
	
	private Map<String, EzyUserRequestHandler> implement(Object controller) {
		Map<String, EzyUserRequestHandler> handlers = new HashMap<>();
		EzyRequestControllerProxy proxy = new EzyRequestControllerProxy(controller);
		String feature = proxy.getFeature();
		for(EzyRequestHandlerMethod method : proxy.getRequestHandlerMethods()) {
			EzyRequestHandlerImplementer implementer = newImplementer(proxy, method);
			EzyAsmRequestHandler handler = implementer.implement();
			String command = handler.getCommand();
			handlers.put(command, handler);
			requestCommandManager.addCommand(command);
			if (proxy.isManagement() || method.isManagement()) {
			    requestCommandManager.addManagementCommand(command);
			}
			if (proxy.isPayment() || method.isPayment()) {
			    requestCommandManager.addPaymentCommand(command);
			}
			String methodFeature = feature != null ? feature : method.getFeature();
			if (EzyStrings.isNotBlank(methodFeature)) {
			    featureCommandManager.addFeatureCommand(methodFeature, command);
			}
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
