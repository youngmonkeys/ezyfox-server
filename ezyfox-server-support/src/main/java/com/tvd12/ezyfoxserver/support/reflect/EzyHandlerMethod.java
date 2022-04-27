package com.tvd12.ezyfoxserver.support.reflect;

import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import lombok.Getter;

import java.lang.reflect.Parameter;

public abstract class EzyHandlerMethod {

    @Getter
    protected final EzyMethod method;

    public EzyHandlerMethod(EzyMethod method) {
        this.method = method;
    }

    public String getName() {
        return method.getName();
    }

    public Parameter[] getParameters() {
        return method.getMethod().getParameters();
    }

    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public Class<?> getRequestDataType() {
        Class<?> dataType = null;
        for (Parameter parameter : getParameters()) {
            Class<?> type = parameter.getType();
            if (type != EzyContext.class &&
                type != EzyAppContext.class &&
                type != EzyPluginContext.class &&
                type != EzyUserSessionEvent.class &&
                type != EzyUser.class &&
                type != EzySession.class &&
                !Throwable.class.isAssignableFrom(type)) {
                if (dataType == null || type != String.class) {
                    dataType = type;
                }
                if (parameter.isAnnotationPresent(EzyRequestData.class)) {
                    break;
                }
            }
        }
        return dataType;
    }
}
