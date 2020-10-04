package com.tvd12.ezyfoxserver.support.reflect;

import java.lang.reflect.Parameter;

import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfox.core.annotation.EzyRequestHandle;
import com.tvd12.ezyfox.core.util.EzyRequestHandleAnnotations;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;

import lombok.Getter;

@Getter
public class EzyRequestHandlerMethod {

	protected final String command;
	protected final EzyMethod method;
	
	public EzyRequestHandlerMethod(String group, EzyMethod method) {
		this.method = method;
		this.command = fetchCommand(group);
	}
	
	protected String fetchCommand(String group) {
		String methodCommand = EzyRequestHandleAnnotations
				.getCommand(method.getAnnotation(EzyRequestHandle.class));
		if(EzyStrings.isNoContent(group))
			return methodCommand;
		return group + "/" + methodCommand;
	}
	
	
	public String getName() {
		return method.getName();
	}
	
	public Parameter[] getParameters() {
		return method.getMethod().getParameters();
	}
	
	public Class<?> getRequestDataType() {
		Class<?> dataType = null;
		for(Parameter parameter : getParameters()) {
			Class<?> type = parameter.getType();
			if(type != EzyContext.class &&
					type != EzyAppContext.class &&
					type != EzyPluginContext.class &&
					type != EzyUserSessionEvent.class &&
					type != EzyUser.class &&
					type != EzySession.class) {
				dataType = type;
				if(type.isAnnotationPresent(EzyRequestData.class))
					break;
			}
		}
		return dataType;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append(method.getName())
				.append("(")
					.append("command: ").append(command)
				.append(")")
				.toString();
	}
}
