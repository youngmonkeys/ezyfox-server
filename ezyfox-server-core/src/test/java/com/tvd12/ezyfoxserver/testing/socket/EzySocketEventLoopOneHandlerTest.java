package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.socket.EzySocketEventHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopOneHandler;

public class EzySocketEventLoopOneHandlerTest {

    @Test
    public void test() throws Exception {
        ExEzySocketEventLoopOneHandler handler = new ExEzySocketEventLoopOneHandler();
        handler.destroy();
        handler.setThreadPoolSize(1);
        handler.setEventHandler(new EzySocketEventHandler() {
            
            @Override
            public void destroy() {
                
            }
            
            @Override
            public void handleEvent() {
                System.out.println("test socket event handler handle event");
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
