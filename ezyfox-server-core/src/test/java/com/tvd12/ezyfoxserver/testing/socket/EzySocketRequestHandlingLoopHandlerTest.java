package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketRequestHandlingLoopHandler;
import org.testng.annotations.Test;

public class EzySocketRequestHandlingLoopHandlerTest {

    @Test
    public void test() throws Exception {
        ExEzySocketRequestHandlingLoopHandler handler = new ExEzySocketRequestHandlingLoopHandler();
        handler.start();
        Thread.sleep(100);
        handler.destroy();
    }

    public static class ExEzySocketRequestHandlingLoopHandler extends EzySocketRequestHandlingLoopHandler {

        @Override
        protected String getRequestType() {
            return "test";
        }
    }
}
