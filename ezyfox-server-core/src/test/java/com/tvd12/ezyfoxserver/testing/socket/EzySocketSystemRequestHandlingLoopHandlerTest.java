package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketSystemRequestHandlingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

public class EzySocketSystemRequestHandlingLoopHandlerTest {

    @Test
    public void test() {
        EzySocketSystemRequestHandlingLoopHandler handler = new EzySocketSystemRequestHandlingLoopHandler();
        MethodInvoker.create()
            .object(handler)
            .method("getRequestType")
            .invoke();
    }

}
