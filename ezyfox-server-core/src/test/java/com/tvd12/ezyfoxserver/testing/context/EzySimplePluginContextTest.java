package com.tvd12.ezyfoxserver.testing.context;

import static org.mockito.Mockito.*;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimplePluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimplePluginContextTest extends BaseTest {

    @Test
    public void test() {
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzySimpleZone zone = new EzySimpleZone();
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);
        when(zoneContext.getParent()).thenReturn(serverContext);
        EzySimplePlugin plugin = new EzySimplePlugin();
        EzySimplePluginSetting setting = new EzySimplePluginSetting();
        plugin.setSetting(setting);
        EzySimplePluginContext pluginContext = new EzySimplePluginContext();
        pluginContext.setParent(zoneContext);
        pluginContext.setPlugin(plugin);
        pluginContext.init();
        assert pluginContext.equals(pluginContext);
        EzySimplePluginContext pluginContext2 = new EzySimplePluginContext();
        assert !pluginContext.equals(pluginContext2);
        assert pluginContext.cmd(EzyPluginResponse.class) != null;
        
        EzySimpleUser user = new EzySimpleUser();
        user.setName("test");
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        user.addSession(session);
        
        EzyData data = EzyEntityFactory.newArrayBuilder()
                .build();
        pluginContext.send(data, session, false);
        pluginContext.send(data, Lists.newArrayList(session), false);
        pluginContext.send(data, user, false);
        pluginContext.send(data, Lists.newArrayList(user), false);
    }
    
}
