package com.tvd12.ezyfoxserver.support.reflect;

import com.tvd12.ezyfox.core.annotation.EzyRequestHandle;
import com.tvd12.ezyfox.core.util.EzyRequestHandleAnnotations;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyMethod;

import lombok.Getter;

@Getter
public class EzyRequestHandlerMethod extends EzyHandlerMethod {

	protected final String command;
	
	public EzyRequestHandlerMethod(String group, EzyMethod method) {
		super(method);
		this.command = fetchCommand(group);
	}
	
	protected String fetchCommand(String group) {
		String methodCommand = EzyRequestHandleAnnotations
				.getCommand(method.getAnnotation(EzyRequestHandle.class));
		if(EzyStrings.isNoContent(group))
			return methodCommand;
		return group + "/" + methodCommand;
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
