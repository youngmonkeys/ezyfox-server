package com.tvd12.ezyfoxserver.testing.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractServerController;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;

public class EzyAbstractServerControllerTest extends BaseCoreTest {
    
    @SuppressWarnings("rawtypes")
    @Test
    public void test() {
        EzySimpleServer server = mock(EzySimpleServer.class);
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        ServerController controller = new ServerController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getZoneContext("example")).thenReturn(zoneContext);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);
        when(serverContext.getAppContext(1)).thenReturn(appContext);
        when(zoneContext.getAppContext("abc")).thenReturn(appContext);
        when(zoneContext.getAppContext(1)).thenReturn(appContext);
        assertEquals(controller.getAppContext(serverContext, 1), appContext);
        when(zoneContext.getAppContext("abc")).thenReturn(appContext);
        
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
