package com.tvd12.ezyfoxserver.support.reflect;

import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EzyExceptionHandlerProxy {

    protected final EzyClass clazz;
    protected final Object instance;
    protected final List<EzyExceptionHandlerMethod> exceptionHandlerMethods;

    public EzyExceptionHandlerProxy(Object instance) {
        this.instance = instance;
        this.clazz = new EzyClass(instance.getClass());
        this.exceptionHandlerMethods = fetchExceptionHandlerMethods();
    }

    public List<EzyExceptionHandlerMethod> fetchExceptionHandlerMethods() {
        List<EzyExceptionHandlerMethod> list = new ArrayList<>();
        List<EzyMethod> methods = clazz.getPublicMethods(m -> m.isAnnotated(EzyTryCatch.class));
        for (EzyMethod method : methods) {
            EzyExceptionHandlerMethod m = new EzyExceptionHandlerMethod(method);
            list.add(m);
        }
        return list;
    }

    public String getClassSimpleName() {
        return clazz.getClazz().getSimpleName();
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(clazz.getName())
            .append("(\n")
            .append("\tinstance: ").append(instance).append(",\n")
            .append("\texceptionHandlerMethods: ").append(exceptionHandlerMethods).append("\n")
            .append(")")
            .toString();
    }
}
