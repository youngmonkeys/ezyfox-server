package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionHandlingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

public class EzySocketDisconnectionHandlingLoopHandlerTest {

    @Test
    public void test() {
        EzySocketDisconnectionHandlingLoopHandler handler = new EzySocketDisconnectionHandlingLoopHandler();
        MethodInvoker.create()
            .object(handler)
            .method("getThreadName")
            .invoke();
    }
}
