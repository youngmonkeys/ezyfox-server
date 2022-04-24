package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySocketExtensionRequestHandlingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySocketExtensionRequestHandlingLoopHandlerTest {

    @Test
    public void test() {
        EzySocketExtensionRequestHandlingLoopHandler handler = new EzySocketExtensionRequestHandlingLoopHandler();
        MethodInvoker.create()
            .object(handler)
            .method("getRequestType")
            .invoke();
    }

}
