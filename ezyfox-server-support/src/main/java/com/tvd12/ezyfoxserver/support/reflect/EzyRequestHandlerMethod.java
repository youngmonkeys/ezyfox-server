package com.tvd12.ezyfoxserver.support.reflect;

import com.tvd12.ezyfox.annotation.EzyFeature;
import com.tvd12.ezyfox.annotation.EzyManagement;
import com.tvd12.ezyfox.annotation.EzyPayment;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.util.EzyDoHandleAnnotations;
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
        String methodCommand = EzyDoHandleAnnotations
            .getCommand(method.getAnnotation(EzyDoHandle.class));
        if (EzyStrings.isNoContent(group)) {
            return methodCommand;
        }
        return group + "/" + methodCommand;
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public boolean isManagement() {
        return method.isAnnotated(EzyManagement.class);
    }

    public boolean isPayment() {
        return method.isAnnotated(EzyPayment.class);
    }

    public String getFeature() {
        EzyFeature annotation = method.getAnnotation(EzyFeature.class);
        return annotation != null ? annotation.value() : null;
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
