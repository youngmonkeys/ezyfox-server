package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginSendResponseImpl;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.reflections.util.Lists;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyPluginSendResponseImplTest extends BaseTest {

    @Test
    public void test() {
        EzyPluginContext pluginContext = mock(EzyPluginContext.class);
        EzyPlugin plugin = mock(EzyPlugin.class);
        when(pluginContext.getPlugin()).thenReturn(plugin);
        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        pluginSetting.setName("test");
        when(plugin.getSetting()).thenReturn(pluginSetting);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(pluginContext.getParent()).thenReturn(zoneContext);
        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(zoneContext.getParent()).thenReturn(serverContext);
        EzyPluginSendResponseImpl cmd = new EzyPluginSendResponseImpl(pluginContext);
        EzyObject data = EzyEntityFactory.newObjectBuilder()
            .build();
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        cmd.execute(data, session, false);
        cmd.execute(data, Lists.newArrayList(session), false);
    }
}
