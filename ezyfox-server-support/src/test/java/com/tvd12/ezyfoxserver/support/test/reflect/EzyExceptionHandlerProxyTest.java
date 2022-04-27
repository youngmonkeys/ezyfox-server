package com.tvd12.ezyfoxserver.support.test.reflect;

import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfoxserver.support.reflect.EzyExceptionHandlerProxy;
import org.testng.annotations.Test;

public class EzyExceptionHandlerProxyTest {

    @Test
    public void test() {
        EzyExceptionHandlerProxy proxy = new EzyExceptionHandlerProxy(
            new ExceptionHandlerEx());
        System.out.println(proxy);
    }

    public static class ExceptionHandlerEx {

        @EzyTryCatch(Exception.class)
        public void handle(Exception e) {
            e.printStackTrace();
        }
    }
}
