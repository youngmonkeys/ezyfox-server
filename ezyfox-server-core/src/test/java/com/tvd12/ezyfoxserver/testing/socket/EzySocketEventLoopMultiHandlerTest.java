package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketEventHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopMultiHandler;
import org.testng.annotations.Test;

public class EzySocketEventLoopMultiHandlerTest {

    @Test
    public void test() throws Exception {
        EzySocketEventLoopMultiHandler handler = new ExEzySocketEventLoopMultiHandler();
        handler.destroy();
        handler.setEventHandlerSupplier(() -> new EzySocketEventHandler() {

            @Override
            public void destroy() {
            }

            @Override
            public void handleEvent() {
            }
        });
        handler.setThreadPoolSize(3);
        handler.start();
        Thread.sleep(200);
        handler.destroy();
        handler.destroy();
    }

    public static class ExEzySocketEventLoopMultiHandler extends EzySocketEventLoopMultiHandler {

        @Override
        protected String getThreadName() {
            return "test";
        }

    }
}
