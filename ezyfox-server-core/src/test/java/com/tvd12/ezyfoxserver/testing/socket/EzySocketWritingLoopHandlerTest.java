package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketWritingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

public class EzySocketWritingLoopHandlerTest {

    @Test
    public void test() {
        EzySocketWritingLoopHandler handler = new EzySocketWritingLoopHandler();
        MethodInvoker.create()
            .object(handler)
            .method("getThreadName")
            .invoke();
    }
}
