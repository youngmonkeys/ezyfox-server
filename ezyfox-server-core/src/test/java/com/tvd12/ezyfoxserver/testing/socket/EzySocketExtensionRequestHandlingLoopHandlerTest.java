package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketExtensionRequestHandlingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

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
