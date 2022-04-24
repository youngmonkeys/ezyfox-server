package com.tvd12.ezyfoxserver.support.test.reflect;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerProxy;

public class EzyExceptionHandlerProxyTest {

    @Test
    public void test() {
        EzyExceptionHandlerProxy proxy = new EzyExceptionHandlerProxy(
                new ExceptionHandlerEx());
        System.out.println(proxy.toString());
    }

    public static class ExceptionHandlerEx {

        @EzyTryCatch(Exception.class)
        public void handle(Exception e) {
            e.printStackTrace();
        }

    }
}
