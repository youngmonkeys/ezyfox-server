package com.tvd12.ezyfoxserver.support.reflect;

import java.util.ArrayList;
import java.util.List;

import com.tvd12.ezyfox.core.annotation.EzyRequestHandle;
import com.tvd12.ezyfox.core.util.EzyClientRequestControllerAnnotations;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;

import lombok.Getter;

@Getter
public class EzyRequestControllerProxy {

	protected final EzyClass clazz;
	protected final Object instance;
	protected final String commandGroup;
	protected final List<EzyRequestHandlerMethod> requestHandlerMethods;
	
	public EzyRequestControllerProxy(Object instance) {
		this.instance = instance;
		this.clazz = new EzyClass(instance.getClass());
		this.commandGroup = getCommandGroup();
		this.requestHandlerMethods = fetchRequestHandlerMethods();
	}
	
	protected String getCommandGroup() {
		String uri = EzyClientRequestControllerAnnotations.getGroup(clazz.getClazz());
		return uri;
	}
	
	protected List<EzyRequestHandlerMethod> fetchRequestHandlerMethods() {
		List<EzyRequestHandlerMethod> list = new ArrayList<>();
		List<EzyMethod> methods = clazz.getPublicMethods(m -> isRequestHandlerMethod(m));
		for(EzyMethod method : methods) {
			EzyRequestHandlerMethod m = new EzyRequestHandlerMethod(commandGroup, method);
			list.add(m);
		}
		return list;
	}
	
	protected boolean isRequestHandlerMethod(EzyMethod method) {
		return method.isAnnotated(EzyRequestHandle.class);
	}
	
	public String getControllerName() {
		return clazz.getClazz().getSimpleName();
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append(clazz.getName())
				.append("(\n")
					.append("\tinstance: ").append(instance).append(",\n")
					.append("\trequestHandlerMethods: ").append(requestHandlerMethods).append(",\n")
				.append(")")
				.toString();
	}
	
}
