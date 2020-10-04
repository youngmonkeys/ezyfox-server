package com.tvd12.ezyfoxserver.support.asm;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfox.asm.EzyFunction;
import com.tvd12.ezyfox.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.reflect.EzyMethods;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestControllerProxy;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestHandlerMethod;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import lombok.Setter;

public class EzyRequestHandlerImplementer extends EzyLoggable {

	@Setter
	private static boolean debug;
	protected final EzyRequestControllerProxy controller;
	protected final EzyRequestHandlerMethod handlerMethod;
	
	protected final static String PARAMETER_PREFIX = "param";
	protected final static AtomicInteger COUNT = new AtomicInteger(0);
	
	public EzyRequestHandlerImplementer(
			EzyRequestControllerProxy controller, EzyRequestHandlerMethod handlerMethod) {
		this.controller = controller;
		this.handlerMethod = handlerMethod;
	}
	
	public EzyAsmRequestHandler implement() {
		try {
			return doimplement();
		}
		catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	protected EzyAsmRequestHandler doimplement() throws Exception {
		ClassPool pool = ClassPool.getDefault();
		String implClassName = getImplClassName();
		CtClass implClass = pool.makeClass(implClassName);
		EzyClass superClass = new EzyClass(getSuperClass());
		String controllerFieldContent = makeControllerFieldContent();
		String setControllerMethodContent = makeSetControllerMethodContent();
		String handleRequestMethodContent = makeHandleRequestMethodContent();
		String getRequestDataTypeMethodContent = makeGetRequestDataTypeMethodContent();
		printComponentContent(controllerFieldContent);
		printComponentContent(setControllerMethodContent);
		printComponentContent(handleRequestMethodContent);
		printComponentContent(getRequestDataTypeMethodContent);
		implClass.setSuperclass(pool.get(superClass.getName()));
		implClass.addField(CtField.make(controllerFieldContent, implClass));
		implClass.addMethod(CtNewMethod.make(setControllerMethodContent, implClass));
		implClass.addMethod(CtNewMethod.make(handleRequestMethodContent, implClass));
		implClass.addMethod(CtNewMethod.make(getRequestDataTypeMethodContent, implClass));
		Class answerClass = implClass.toClass();
		implClass.detach();
		EzyAsmRequestHandler handler = (EzyAsmRequestHandler) answerClass.newInstance();
		handler.setCommand(handlerMethod.getCommand());
		setRepoComponent(handler);
		return handler;
	}
	
	protected void setRepoComponent(EzyAsmRequestHandler handler) {
		handler.setController(controller.getInstance());
	}
	
	protected String makeControllerFieldContent() {
		return new EzyInstruction()
				.append("private ")
					.append(controller.getClazz().getName())
						.append(" controller")
				.toString();
	}
	
	protected String makeSetControllerMethodContent() {
		return new EzyFunction(getSetControllerMethod())
				.body()
					.append(new EzyInstruction("\t", "\n")
							.append("this.controller")
							.equal()
							.brackets(controller.getClazz().getClazz())
							.append("arg0"))
					.function()
				.toString();
	}
	
	protected String makeHandleRequestMethodContent() {
		EzyMethod method = getHandleRequestMethod();
		EzyFunction function = new EzyFunction(method)
				.throwsException();
		EzyBody body = function.body();
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
				instruction.cast(requestDataType, "arg2");
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
			body.append(instruction);
			++ paramCount;
			
		}
		EzyInstruction instruction = new EzyInstruction("\t", "\n");
		StringBuilder answerExpression = new StringBuilder();
		answerExpression.append("this.controller.").append(handlerMethod.getName())
				.append("(");
		for(int i = 0 ; i < paramCount ; ++i) {
			answerExpression.append(PARAMETER_PREFIX).append(i);
			if(i < paramCount - 1)
				answerExpression.append(", ");
		}
		answerExpression.append(")");
		instruction.append(answerExpression);
		body.append(instruction);
		return function.toString();
	}
	
	protected String makeGetRequestDataTypeMethodContent() {
		return new EzyFunction(getGetResponseContentTypeMethod())
				.body()
					.append(new EzyInstruction("\t", "\n")
							.answer()
							.clazz(handlerMethod.getRequestDataType(), true))
					.function()
				.toString();
	}
	
	protected EzyMethod getSetControllerMethod() {
		Method method = EzyMethods.getMethod(
				EzyAsmAbstractRequestHandler.class, "setController", Object.class);
		return new EzyMethod(method);
	}
	
	protected EzyMethod getHandleRequestMethod() {
		Method method = EzyMethods.getMethod(
				EzyAsmAbstractRequestHandler.class, 
				"handleRequest",
				EzyContext.class, EzyUserSessionEvent.class, Object.class);
		return new EzyMethod(method);
	}
	
	protected EzyMethod getGetResponseContentTypeMethod() {
		Method method = EzyMethods.getMethod(
				EzyAsmAbstractRequestHandler.class, "getDataType");
		return new EzyMethod(method);
	}
	
	protected Class<?> getSuperClass() {
		return EzyAsmAbstractRequestHandler.class;
	}
	
	protected String getImplClassName() {
		return controller.getControllerName()
				+ "$" + handlerMethod.getName() + "$Handler$AutoImpl$" + COUNT.incrementAndGet();
	}
	
	protected void printComponentContent(String componentContent) {
		if(debug) 
			logger.debug("component content: \n{}", componentContent);
	}
	
}
