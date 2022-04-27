package com.tvd12.ezyfoxserver.support.reflect;

import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfox.core.util.EzyTryCatchAnnotations;
import com.tvd12.ezyfox.reflect.EzyMethod;
import lombok.Getter;

@Getter
public class EzyExceptionHandlerMethod extends EzyHandlerMethod {

    protected final Class<?>[] exceptionClasses;

    public EzyExceptionHandlerMethod(EzyMethod method) {
        super(method);
        this.exceptionClasses = fetchExceptionClasses();
    }

    protected Class<?>[] fetchExceptionClasses() {
        EzyTryCatch annotation = method.getAnnotation(EzyTryCatch.class);
        return EzyTryCatchAnnotations.getExceptionClasses(annotation);
    }
}
