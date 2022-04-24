package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketUserRemovalHandlingLoopHandler;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

public class EzySocketUserRemovalHandlingLoopHandlerTest {

    @Test
    public void test() {
        EzySocketUserRemovalHandlingLoopHandler handler = new EzySocketUserRemovalHandlingLoopHandler();
        MethodInvoker.create()
            .object(handler)
            .method("getThreadName")
            .invoke();
    }

}
