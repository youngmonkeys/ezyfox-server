package com.tvd12.ezyfoxserver.support.test.asm;

import com.tvd12.ezyfox.core.annotation.EzyExceptionHandler;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.support.asm.EzyExceptionHandlerImplementer;
import com.tvd12.ezyfoxserver.support.asm.EzyExceptionHandlersImplementer;
import com.tvd12.ezyfoxserver.support.handler.EzyUncaughtExceptionHandler;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerMethod;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerProxy;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;

public class EzyExceptionHandlersImplementerTest {

    @Test
    public void test() {
        EzyExceptionHandlerImplementer.setDebug(true);
        EzyExceptionHandlersImplementer implementer = new EzyExceptionHandlersImplementer();
        implementer.implement(Collections.singletonList(new ExExceptionHandler()));
        EzyExceptionHandlerImplementer.setDebug(false);
        implementer.implement(Collections.singletonList(new ExExceptionHandler()));
    }

    @Test
    public void implementWithNullHandler() {
        // given
        EzyExceptionHandlersImplementer implementer = new EzyExceptionHandlersImplementer() {
            @Override
            protected EzyExceptionHandlerImplementer newImplementer(
                EzyExceptionHandlerProxy exceptionHandler,
                EzyExceptionHandlerMethod method
            ) {
                return new EzyExceptionHandlerImplementer(exceptionHandler, method) {
                    @Override
                    public EzyUncaughtExceptionHandler implement() {
                        return null;
                    }
                };
            }
        };

        // when
        Map<Class<?>, EzyUncaughtExceptionHandler> handlers = implementer.implement(
            Collections.singletonList(new ExExceptionHandler())
        );

        // then
        Asserts.assertEquals(handlers.size(), 0);
    }

    @EzyExceptionHandler
    public static class ExExceptionHandler {

        @EzyTryCatch(IllegalArgumentException.class)
        public void handle(
            @SuppressWarnings("unused") EzySession session,
            IllegalArgumentException ex
        ) {
            ex.printStackTrace();
        }
    }
}
