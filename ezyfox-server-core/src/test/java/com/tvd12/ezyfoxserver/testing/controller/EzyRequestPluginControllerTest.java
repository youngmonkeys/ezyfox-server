package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyRequestPluginController;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestPluginRequest;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyRequestPluginControllerTest extends BaseTest {

    @Test
    public void test() {
        EzyRequestPluginController controller = new EzyRequestPluginController();

        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);
        EzyPluginContext pluginContext = mock(EzyPluginContext.class);
        when(zoneContext.getPluginContext(1)).thenReturn(pluginContext);
        EzyPlugin plugin = mock(EzyPlugin.class);
        when(pluginContext.getPlugin()).thenReturn(plugin);
        EzyPluginRequestController requestController = mock(EzyPluginRequestController.class);
        when(plugin.getRequestController()).thenReturn(requestController);

        EzySimpleRequestPluginRequest request = new EzySimpleRequestPluginRequest();
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzySimpleUser user = new EzySimpleUser();
        user.setZoneId(1);
        user.setId(1);
        user.setName("test");
        request.setSession(session);
        request.setUser(user);
        EzyArray array = EzyEntityFactory.newArrayBuilder()
            .append(1)
            .append(EzyEntityFactory.newArrayBuilder())
            .build();
        request.deserializeParams(array);
        controller.handle(serverContext, request);
    }
}
