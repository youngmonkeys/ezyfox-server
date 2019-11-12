package com.tvd12.ezyfoxserver.testing.command;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.command.impl.EzyBroadcastPluginsEventImpl;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerReadyEvent;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting.EzySimpleListenEvents;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

public class EzyBroadcastPluginsEventImplTest extends BaseTest {

    @Test
    public void pluginContextsNullCaseTest() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzySimpleZoneSetting setting = new EzySimpleZoneSetting();
        setting.setName("test");
        when(zone.getSetting()).thenReturn(setting);
        EzyBroadcastPluginsEventImpl cmd = new EzyBroadcastPluginsEventImpl(zoneContext);
        EzyServerReadyEvent event = new EzySimpleServerReadyEvent();
        cmd.fire(EzyEventType.SERVER_READY, event, true);
    }
    
    @Test
    public void firePluginEventExceptionCase() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzySimpleZoneSetting setting = new EzySimpleZoneSetting();
        setting.setName("test");
        when(zone.getSetting()).thenReturn(setting);
        
        EzyPluginContext pluginContext = mock(EzyPluginContext.class);
        EzyPlugin plugin = mock(EzyPlugin.class);
        when(pluginContext.getPlugin()).thenReturn(plugin);
        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        when(plugin.getSetting()).thenReturn(pluginSetting);
        EzySimpleListenEvents listenEvents = pluginSetting.getListenEvents();
        listenEvents.setEvent("SERVER_READY");
        doThrow(new IllegalStateException("server maintain")).when(pluginContext).handleEvent(any(), any());
        
        when(zoneContext.getPluginContexts()).thenReturn(Lists.newArrayList(pluginContext));
        
        EzyBroadcastPluginsEventImpl cmd = new EzyBroadcastPluginsEventImpl(zoneContext);
        EzyServerReadyEvent event = new EzySimpleServerReadyEvent();
        cmd.fire(EzyEventType.SERVER_READY, event, true);
    }
    
}
