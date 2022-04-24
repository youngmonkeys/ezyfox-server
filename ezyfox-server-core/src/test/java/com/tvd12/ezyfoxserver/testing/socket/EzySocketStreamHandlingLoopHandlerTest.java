package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySocketStreamHandlingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;

public class EzySocketStreamHandlingLoopHandlerTest {

    @Test
    public void test() {
        EzySocketStreamHandlingLoopHandler handler = new EzySocketStreamHandlingLoopHandler();
        MethodInvoker.create()
            .object(handler)
            .method("getThreadName")
            .invoke();
    }

}
