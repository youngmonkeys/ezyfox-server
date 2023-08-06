package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketEventHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopOneHandler;
import org.testng.annotations.Test;

public class EzySocketEventLoopOneHandlerTest {

    @Test
    public void test() throws Exception {
        ExEzySocketEventLoopOneHandler handler = new ExEzySocketEventLoopOneHandler();
        handler.destroy();
        handler.setThreadPoolSize(1);
        handler.setEventHandler(new EzySocketEventHandler() {

            @Override
            public void destroy() {}

            @Override
            public void handleEvent() {
                System.out.println("test socket event handler handle event");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        handler.start();
        Thread.sleep(100);
        handler.destroy();
    }

    public static class ExEzySocketEventLoopOneHandler extends EzySocketEventLoopOneHandler {

        @Override
        protected String getThreadName() {
            return "test";
        }
    }
}
