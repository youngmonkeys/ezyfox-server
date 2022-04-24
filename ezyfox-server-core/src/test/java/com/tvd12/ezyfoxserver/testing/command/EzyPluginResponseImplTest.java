package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.command.impl.EzyPluginResponseImpl;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyPluginResponseImplTest extends BaseTest {

    @Test
    public void test() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager userManager = EzyZoneUserManagerImpl.builder()
            .build();
        when(zone.getUserManager()).thenReturn(userManager);
        EzyPluginContext pluginContext = mock(EzyPluginContext.class);
        when(pluginContext.getParent()).thenReturn(zoneContext);
        EzyPluginResponse cmd = (EzyPluginResponse) new EzyPluginResponseImpl(pluginContext)
            .command("test")
            .params(EzyEntityFactory.newArrayBuilder());
        cmd.execute();
    }
}
