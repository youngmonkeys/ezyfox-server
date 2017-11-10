package com.tvd12.ezyfoxserver.testing.controller;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractServerController;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import static org.mockito.Mockito.*;

public class EzyAbstractServerControllerTest extends BaseCoreTest {
    
    @SuppressWarnings("rawtypes")
    @Test
    public void test() {
        EzySimpleServer server = mock(EzySimpleServer.class);
        EzyAppContext appContext = mock(EzyAppContext.class);
        ServerController controller = new ServerController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getAppContext(1)).thenReturn(appContext);
        assertEquals(controller.getAppContext(serverContext, 1), appContext);
        when(serverContext.getAppContext("abc")).thenReturn(appContext);
        assertEquals(controller.getAppContext(serverContext, "abc"), appContext);
        
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        
        when(server.getControllers()).thenReturn(controllers);
        when(serverContext.getServer()).thenReturn(server);
        
        EzyController ctr = mock(EzyController.class);
        when(controllers.getController(EzyCommand.APP_ACCESS)).thenReturn(ctr);
        assertEquals(controller.getControllers(serverContext), controllers);
        assertEquals(controller.getController(serverContext, EzyCommand.APP_ACCESS), ctr);
    }

    private static class ServerController extends EzyAbstractServerController {
        @Override
        public EzyAppContext getAppContext(EzyServerContext ctx, int appId) {
            return super.getAppContext(ctx, appId);
        }
        
        @Override
        public EzyAppContext getAppContext(EzyServerContext ctx, String appName) {
            return super.getAppContext(ctx, appName);
        }
        
        @Override
        public EzyServerControllers getControllers(EzyServerContext ctx) {
            return super.getControllers(ctx);
        }
        
        @SuppressWarnings("rawtypes")
        @Override
        public EzyController getController(EzyServerContext ctx, EzyConstant cmd) {
            return super.getController(ctx, cmd);
        }
    }
    
}
