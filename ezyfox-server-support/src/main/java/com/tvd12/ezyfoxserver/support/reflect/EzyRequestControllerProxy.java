package com.tvd12.ezyfoxserver.support.reflect;

import com.tvd12.ezyfox.annotation.EzyFeature;
import com.tvd12.ezyfox.annotation.EzyManagement;
import com.tvd12.ezyfox.annotation.EzyPayment;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfox.core.util.EzyRequestControllerAnnotations;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EzyRequestControllerProxy {

    protected final EzyClass clazz;
    protected final Object instance;
    protected final String commandGroup;
    protected final List<EzyRequestHandlerMethod> requestHandlerMethods;
    protected final List<EzyExceptionHandlerMethod> exceptionHandlerMethods;
    protected final Map<Class<?>, EzyExceptionHandlerMethod> exceptionHandlerMethodMap;

    public EzyRequestControllerProxy(Object instance) {
        this.instance = instance;
        this.clazz = new EzyClass(instance.getClass());
        this.commandGroup = getCommandGroup();
        this.requestHandlerMethods = fetchRequestHandlerMethods();
        this.exceptionHandlerMethods = fetchExceptionHandlerMethods();
        this.exceptionHandlerMethodMap = fetchExceptionHandlerMethodMap();
    }

    protected String getCommandGroup() {
        String uri = EzyRequestControllerAnnotations.getGroup(clazz.getClazz());
        return uri;
    }

    protected List<EzyRequestHandlerMethod> fetchRequestHandlerMethods() {
        List<EzyRequestHandlerMethod> list = new ArrayList<>();
        List<EzyMethod> methods = clazz.getPublicMethods(m -> isRequestHandlerMethod(m));
        for (EzyMethod method : methods) {
            EzyRequestHandlerMethod m = new EzyRequestHandlerMethod(commandGroup, method);
            list.add(m);
        }
        return list;
    }

    public List<EzyExceptionHandlerMethod> fetchExceptionHandlerMethods() {
        List<EzyExceptionHandlerMethod> list = new ArrayList<>();
        List<EzyMethod> methods = clazz.getMethods(m -> m.isAnnotated(EzyTryCatch.class));
        for (EzyMethod method : methods) {
            EzyExceptionHandlerMethod m = new EzyExceptionHandlerMethod(method);
            list.add(m);
        }
        return list;
    }

    protected final Map<Class<?>, EzyExceptionHandlerMethod> fetchExceptionHandlerMethodMap() {
        Map<Class<?>, EzyExceptionHandlerMethod> answer = new HashMap<>();
        for (EzyExceptionHandlerMethod m : exceptionHandlerMethods) {
            for (Class<?> exceptionClass : m.getExceptionClasses()) {
                answer.put(exceptionClass, m);
            }
        }
        return answer;
    }

    protected boolean isRequestHandlerMethod(EzyMethod method) {
        return method.isAnnotated(EzyDoHandle.class);
    }

    public String getControllerName() {
        return clazz.getClazz().getSimpleName();
    }

    public boolean isManagement() {
        return clazz.isAnnotated(EzyManagement.class);
    }

    public boolean isPayment() {
        return clazz.isAnnotated(EzyPayment.class);
    }

    public String getFeature() {
        EzyFeature annotation = clazz.getAnnotation(EzyFeature.class);
        return annotation != null ? annotation.value() : null;
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
