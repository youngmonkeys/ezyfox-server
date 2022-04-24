package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyPingController;
import com.tvd12.ezyfoxserver.request.EzySimplePingRequest;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyPingControllerTest extends BaseTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void test() {
        EzyPingController controller = new EzyPingController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzySimplePingRequest request = new EzySimplePingRequest();
        controller.handle(serverContext, request);

        EzySimpleSettings settings = new EzySimpleSettings();
        EzyServer server = mock(EzyServer.class);
        when(server.getSettings()).thenReturn(settings);
        when(serverContext.getServer()).thenReturn(server);

        assert MethodInvoker.create()
            .object(controller)
            .method("getSettings")
            .param(EzyServerContext.class, serverContext)
            .invoke() == settings;

        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);

        assert MethodInvoker.create()
            .object(controller)
            .method("getSessionManager")
            .param(EzyServerContext.class, serverContext)
            .invoke() == sessionManager;

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);

        assert MethodInvoker.create()
            .object(controller)
            .method("getUserManager")
            .param(EzyZoneContext.class, zoneContext)
            .invoke() == zoneUserManager;
    }

}
