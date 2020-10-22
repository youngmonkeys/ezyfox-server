package com.tvd12.ezyfoxserver.support.asm;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfox.asm.EzyFunction;
import com.tvd12.ezyfox.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyClassTree;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.reflect.EzyMethods;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyAbstractUncaughtExceptionHandler;
import com.tvd12.ezyfoxserver.support.handler.EzyUncaughtExceptionHandler;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerMethod;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerProxy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import lombok.Setter;

@SuppressWarnings("rawtypes")
public class EzyExceptionHandlerImplementer 
		extends EzyAbstractHandlerImplementer<EzyExceptionHandlerMethod> {

	@Setter
	private static boolean debug;
	protected final EzyExceptionHandlerProxy exceptionHandler;
	
	protected final static String PARAMETER_PREFIX = "param";
	protected final static AtomicInteger COUNT = new AtomicInteger(0);
	
	public EzyExceptionHandlerImplementer(
			EzyExceptionHandlerProxy exceptionHandler, EzyExceptionHandlerMethod handlerMethod) {
		super(handlerMethod);
		this.exceptionHandler = exceptionHandler;
	}
	
	public EzyUncaughtExceptionHandler implement() {
		try {
			return doimplement();
		}
		catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}

	protected EzyUncaughtExceptionHandler doimplement() throws Exception {
		ClassPool pool = ClassPool.getDefault();
		String implClassName = getImplClassName();
		CtClass implClass = pool.makeClass(implClassName);
		EzyClass superClass = new EzyClass(getSuperClass());
		String exceptionHandlerFieldContent = makeExceptionHandlerFieldContent();
		String setExceptionHandlerMethodContent = makeSetExceptionHandlerMethodContent();
		String handleExceptionMethodContent = makeHandleExceptionMethodContent();
		printComponentContent(implClassName);
		printComponentContent(exceptionHandlerFieldContent);
		printComponentContent(setExceptionHandlerMethodContent);
		printComponentContent(handleExceptionMethodContent);
		implClass.setSuperclass(pool.get(superClass.getName()));
		implClass.addField(CtField.make(exceptionHandlerFieldContent, implClass));
		implClass.addMethod(CtNewMethod.make(setExceptionHandlerMethodContent, implClass));
		implClass.addMethod(CtNewMethod.make(handleExceptionMethodContent, implClass));
		Class answerClass = implClass.toClass();
		implClass.detach();
		EzyUncaughtExceptionHandler handler = (EzyUncaughtExceptionHandler) answerClass.newInstance();
		setRepoComponent(handler);
		return handler;
	}
	
	protected void setRepoComponent(EzyUncaughtExceptionHandler handler) {
		handler.setExceptionHandler(exceptionHandler.getInstance());
	}
	
	protected String makeExceptionHandlerFieldContent() {
		return new EzyInstruction()
				.append("private ")
					.append(exceptionHandler.getClazz().getName())
						.append(" exceptionHandler")
				.toString();
	}
	
	protected String makeSetExceptionHandlerMethodContent() {
		return new EzyFunction(getSetExceptionHandlerMethod())
				.body()
					.append(new EzyInstruction("\t", "\n")
							.append("this.exceptionHandler")
							.equal()
							.brackets(exceptionHandler.getClazz().getClazz())
							.append("arg0"))
					.function()
				.toString();
	}
	
	protected String makeHandleExceptionMethodContent() {
		EzyMethod method = getHandleExceptionMethod();
		EzyFunction function = new EzyFunction(method)
				.throwsException();
		EzyBody body = function.body();
		int paramCount = prepareHandleMethodArguments(body, true);
		Class<?>[] exceptionClasses = handlerMethod.getExceptionClasses();
		EzyClassTree exceptionTree = new EzyClassTree(exceptionClasses);
		for(Class<?> exceptionClass : exceptionTree.toList()) {
			EzyInstruction instructionIf = new EzyInstruction("\t", "\n", false)
					.append("if(arg4 instanceof ")
						.append(exceptionClass.getName())
					.append(") {");
			body.append(instructionIf);
			EzyInstruction instructionHandle = new EzyInstruction("\t\t", "\n");
			instructionHandle
					.append("this.exceptionHandler.").append(handlerMethod.getName())
					.bracketopen();
			for(int i = 0 ; i < paramCount ; ++i) {
				instructionHandle.append(PARAMETER_PREFIX)
					.append(i)
					.append(", ");
			}
			instructionHandle
					.brackets(exceptionClass).append("arg4")
					.bracketclose();
			body.append(instructionHandle);
			body.append(new EzyInstruction("\t", "\n", false).append("}"));
		}
		if(exceptionClasses.length > 0) {
			body.append(new EzyInstruction("\t", "\n", false).append("else {"));
			body.append(new EzyInstruction("\t\t", "\n").append("throw arg4"));
			body.append(new EzyInstruction("\t", "\n", false).append("}"));
		}
		else {
			body.append(new EzyInstruction("\t", "\n").append("throw arg4"));
		}
		return function.toString();
	}
	
	protected EzyMethod getSetExceptionHandlerMethod() {
		Method method = EzyMethods.getMethod(
				EzyAbstractUncaughtExceptionHandler.class, "setExceptionHandler", Object.class);
		return new EzyMethod(method);
	}
	
	protected EzyMethod getHandleExceptionMethod() {
		Method method = EzyMethods.getMethod(
				EzyAbstractUncaughtExceptionHandler.class, 
				"handleException", 
				EzyContext.class, EzyUserSessionEvent.class, String.class, Object.class, Exception.class);
		return new EzyMethod(method);
	}
	
	protected Class<?> getSuperClass() {
		return EzyAbstractUncaughtExceptionHandler.class;
	}
	
	protected String getImplClassName() {
		return exceptionHandler.getClassSimpleName()
				+ "$" + handlerMethod.getName() + "$ExceptionHandler$AutoImpl$" + COUNT.incrementAndGet();
	}
	
	protected void printComponentContent(String componentContent) {
		if(debug) 
			logger.debug("component content: \n{}", componentContent);
	}
	
}
