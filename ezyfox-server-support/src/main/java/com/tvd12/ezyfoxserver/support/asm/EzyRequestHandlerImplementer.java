package com.tvd12.ezyfoxserver.support.asm;

import com.tvd12.ezyfox.asm.EzyFunction;
import com.tvd12.ezyfox.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyClassTree;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.reflect.EzyMethods;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerMethod;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestControllerProxy;
import com.tvd12.ezyfoxserver.support.reflect.EzyRequestHandlerMethod;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class EzyRequestHandlerImplementer
    extends EzyAbstractHandlerImplementer<EzyRequestHandlerMethod> {

    protected final static AtomicInteger COUNT = new AtomicInteger(0);
    @Setter
    private static boolean debug;
    protected final EzyRequestControllerProxy controller;
    @Setter
    protected EzyResponseFactory responseFactory;

    public EzyRequestHandlerImplementer(
        EzyRequestControllerProxy controller, EzyRequestHandlerMethod handlerMethod) {
        super(handlerMethod);
        this.controller = controller;
    }

    public EzyAsmRequestHandler implement() {
        try {
            return doimplement();
        } catch (Exception e) {
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
        String handleExceptionMethodContent = makeHandleExceptionMethodContent();
        String getRequestDataTypeMethodContent = makeGetRequestDataTypeMethodContent();
        printComponentContent(controllerFieldContent);
        printComponentContent(setControllerMethodContent);
        printComponentContent(handleRequestMethodContent);
        printComponentContent(handleExceptionMethodContent);
        printComponentContent(getRequestDataTypeMethodContent);
        implClass.setSuperclass(pool.get(superClass.getName()));
        implClass.addField(CtField.make(controllerFieldContent, implClass));
        implClass.addMethod(CtNewMethod.make(setControllerMethodContent, implClass));
        implClass.addMethod(CtNewMethod.make(handleRequestMethodContent, implClass));
        implClass.addMethod(CtNewMethod.make(handleExceptionMethodContent, implClass));
        implClass.addMethod(CtNewMethod.make(getRequestDataTypeMethodContent, implClass));
        Class answerClass = implClass.toClass();
        implClass.detach();
        EzyAsmRequestHandler handler = (EzyAsmRequestHandler) answerClass.newInstance();
        handler.setCommand(handlerMethod.getCommand());
        handler.setResponseFactory(responseFactory);
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
        int paramCount = prepareHandleMethodArguments(body);
        EzyInstruction instruction = new EzyInstruction("\t", "\n");
        StringBuilder answerExpression = new StringBuilder();
        if (handlerMethod.getReturnType() != void.class) {
            answerExpression.append(Object.class.getName())
                .append(" data = ");
        }
        answerExpression.append("this.controller.").append(handlerMethod.getName())
            .append("(");
        for (int i = 0; i < paramCount; ++i) {
            answerExpression.append(PARAMETER_PREFIX).append(i);
            if (i < paramCount - 1) {
                answerExpression.append(", ");
            }
        }
        answerExpression.append(")");
        instruction.append(answerExpression);
        body.append(instruction);
        if (handlerMethod.getReturnType() != void.class) {
            EzyInstruction responseInstruction = new EzyInstruction("\t", "\n")
                .invoke("this", "responseToSession", "arg1", "data");
            body.append(responseInstruction);
        }
        return function.toString();
    }

    protected String makeHandleExceptionMethodContent() {
        EzyMethod method = getHandleExceptionMethod();
        EzyFunction function = new EzyFunction(method)
            .throwsException();
        EzyBody body = function.body();
        Map<Class<?>, EzyExceptionHandlerMethod> exceptionHandlerMethodMap
            = controller.getExceptionHandlerMethodMap();
        Set<Class<?>> exceptionClasses = exceptionHandlerMethodMap.keySet();
        EzyClassTree exceptionTree = new EzyClassTree(exceptionClasses);
        for (Class<?> exceptionClass : exceptionTree.toList()) {
            EzyExceptionHandlerMethod m = exceptionHandlerMethodMap.get(exceptionClass);
            EzyInstruction instructionIf = new EzyInstruction("\t", "\n", false)
                .append("if(arg3 instanceof ")
                .append(exceptionClass.getName())
                .append(") {");
            body.append(instructionIf);
            EzyInstruction instructionHandle = new EzyInstruction("\t\t", "\n");
            instructionHandle
                .append("this.controller.").append(m.getName())
                .bracketopen();
            appendHandleExceptionMethodArguments(m, instructionHandle, exceptionClass);
            instructionHandle
                .bracketclose();
            body.append(instructionHandle);
            body.append(new EzyInstruction("\t", "\n", false).append("}"));
        }
        if (exceptionClasses.size() > 0) {
            body.append(new EzyInstruction("\t", "\n", false).append("else {"));
            body.append(new EzyInstruction("\t\t", "\n").append("throw arg3"));
            body.append(new EzyInstruction("\t", "\n", false).append("}"));
        } else {
            body.append(new EzyInstruction("\t", "\n").append("throw arg3"));
        }
        return function.toString();
    }

    protected void appendHandleExceptionMethodArguments(
        EzyExceptionHandlerMethod method,
        EzyInstruction instruction, Class<?> exceptionClass) {
        super.appendHandleExceptionMethodArguments(
            method, instruction, exceptionClass, "this.command", "arg2", "arg3");
    }

    protected String makeGetRequestDataTypeMethodContent() {
        EzyInstruction answerInstruction = new EzyInstruction("\t", "\n")
            .answer();
        if (handlerMethod.getRequestDataType() == null) {
            answerInstruction.append("null");
        } else {
            answerInstruction.clazz(handlerMethod.getRequestDataType(), true);
        }
        return new EzyFunction(getGetResponseContentTypeMethod())
            .body()
            .append(answerInstruction)
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

    protected EzyMethod getHandleExceptionMethod() {
        Method method = EzyMethods.getMethod(
            EzyAsmAbstractRequestHandler.class,
            "handleException",
            EzyContext.class, EzyUserSessionEvent.class, Object.class, Exception.class);
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
        if (debug) {
            logger.debug("component content: \n{}", componentContent);
        }
    }
}
