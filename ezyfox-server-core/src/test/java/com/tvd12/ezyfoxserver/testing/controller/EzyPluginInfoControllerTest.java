package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyPluginInfoController;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.request.EzySimplePluginInfoRequest;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyPluginInfoControllerTest extends BaseTest {

    @Test
    public void test() {
        EzyPluginInfoController controller = new EzyPluginInfoController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);
        EzySimplePluginInfoRequest request = new EzySimplePluginInfoRequest();
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzySimpleUser user = new EzySimpleUser();
        user.setZoneId(1);
        request.setSession(session);
        request.setUser(user);
        EzyArray data = EzyEntityFactory.newArrayBuilder()
            .append("test")
            .build();
        request.deserializeParams(data);
        controller.handle(serverContext, request);

        EzyPluginContext pluginContext = mock(EzyPluginContext.class);
        EzySimplePlugin plugin = new EzySimplePlugin();
        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        plugin.setSetting(pluginSetting);
        when(pluginContext.getPlugin()).thenReturn(plugin);
        when(zoneContext.getPluginContext("test")).thenReturn(pluginContext);
        controller.handle(serverContext, request);
    }
}
