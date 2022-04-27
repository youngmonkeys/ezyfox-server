package com.tvd12.ezyfoxserver.support.test.reflect;

import com.tvd12.ezyfoxserver.support.reflect.EzyRequestControllerProxy;
import com.tvd12.ezyfoxserver.support.test.controller.HelloController;
import org.testng.annotations.Test;

public class EzyRequestControllerTest {

    @Test
    public void test() {
        Object instance = new HelloController();
        EzyRequestControllerProxy controllerProxy =
            new EzyRequestControllerProxy(instance);
        assert controllerProxy.getInstance() == instance;
        //noinspection ConstantConditions
        assert controllerProxy.getExceptionHandlerMethods().size() >= 0;
        System.out.println(controllerProxy);
    }
}
