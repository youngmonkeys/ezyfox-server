package com.tvd12.ezyfoxserver.support.asm;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.support.handler.EzyUncaughtExceptionHandler;

public interface EzyAsmUncaughtExceptionHandler<C extends EzyContext, D>
        extends EzyUncaughtExceptionHandler<C, D> {

    void setExceptionHandler(Object exceptionHandler);

}
