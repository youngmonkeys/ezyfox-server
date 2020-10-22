package com.tvd12.ezyfoxserver.support.asm;

import java.lang.reflect.Parameter;

import com.tvd12.ezyfox.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.reflect.EzyHandlerMethod;

public class EzyAbstractHandlerImplementer<H extends EzyHandlerMethod> 
		extends EzyLoggable {
	
	protected final H handlerMethod;
	
	protected final static String PARAMETER_PREFIX = "param";
	
	public EzyAbstractHandlerImplementer(H handlerMethod) {
		this.handlerMethod = handlerMethod;
	}

	protected int prepareHandleMethodArguments(EzyBody body, boolean exceptionHandle) {
		int paramCount = 0;
		Class<?> requestDataType = handlerMethod.getRequestDataType();
		Parameter[] parameters = handlerMethod.getParameters();
		for(Parameter parameter : parameters) {
			Class<?> parameterType = parameter.getType();
			EzyInstruction instruction = new EzyInstruction("\t", "\n")
					.clazz(parameterType)
					.append(" ").append(PARAMETER_PREFIX).append(paramCount)
					.equal();
			if(parameterType == requestDataType) {
				instruction.cast(requestDataType, exceptionHandle ? "arg3" : "arg2");
			}
			else if(EzyContext.class.isAssignableFrom(parameterType)) {
				instruction.append("arg0");
			}
			else if(parameterType == EzyUserSessionEvent.class) {
				instruction.append("arg1");
			}
			else if(parameterType == EzyUser.class) {
				instruction.append("arg1.getUser()");
			}
			else if(parameterType == EzySession.class) {
				instruction.append("arg1.getSession()");
			}
			else if(parameterType == String.class) {
				instruction.cast(requestDataType, exceptionHandle ? "arg2" : "null");
					
			}
			else if(Throwable.class.isAssignableFrom(parameterType)) {
				if(exceptionHandle)
					continue;
				else 
					instruction.append("null");
			}
			else {
				if(parameterType == boolean.class)
					instruction.append("false");
				else if(parameterType.isPrimitive())
					instruction.append("0");
				else 
					instruction.append("null");
			}
			body.append(instruction);
			++ paramCount;
		}
		return paramCount;
	}
	
}
