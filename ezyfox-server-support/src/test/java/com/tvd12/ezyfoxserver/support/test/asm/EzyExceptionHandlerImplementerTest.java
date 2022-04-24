package com.tvd12.ezyfoxserver.support.test.asm;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.support.asm.EzyExceptionHandlerImplementer;
import com.tvd12.ezyfoxserver.support.handler.EzyUncaughtExceptionHandler;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerMethod;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerProxy;

public class EzyExceptionHandlerImplementerTest {

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFailedCase() throws Exception {
        EzyExceptionHandlerProxy handlerProxy =
                new EzyExceptionHandlerProxy(new ExceptionHandlerFail());
        EzyExceptionHandlerMethod method =
                new EzyExceptionHandlerMethod(new EzyMethod(
                        ExceptionHandlerFail.class
                        .getDeclaredMethod("handle", Exception.class, int.class)));
        new EzyExceptionHandlerImplementer(handlerProxy, method) {
            @SuppressWarnings("rawtypes")
            @Override
            protected EzyUncaughtExceptionHandler doimplement() throws Exception {
                throw new IllegalStateException("test");
            }
        }
        .implement();

    }

    public static class ExceptionHandlerFail {

        @EzyTryCatch(Exception.class)
        public void handle(Exception e,
                @EzyRequestData int value) {
            e.printStackTrace();
        }

    }
}
