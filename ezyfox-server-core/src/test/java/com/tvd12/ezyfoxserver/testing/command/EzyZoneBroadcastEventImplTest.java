package com.tvd12.ezyfoxserver.testing.command;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.command.impl.EzyZoneBroadcastEventImpl;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerReadyEvent;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

public class EzyZoneBroadcastEventImplTest extends BaseTest {

    @Test
    public void fireAppEventNoCatchExceptionCaseTest() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzySimpleZoneSetting setting = new EzySimpleZoneSetting();
        setting.setName("test");
        when(zone.getSetting()).thenReturn(setting);
        EzyAppContext appContext = mock(EzyAppContext.class);
        when(zoneContext.getAppContexts()).thenReturn(Lists.newArrayList(appContext));
        
        EzyZoneBroadcastEventImpl cmd = new EzyZoneBroadcastEventImpl(zoneContext);
        EzyServerReadyEvent event = new EzySimpleServerReadyEvent();
        cmd.fire(EzyEventType.SERVER_READY, event, false);
    }
    
    @Test
    public void fireAppEventCatchExceptionCaseTest() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzySimpleZoneSetting setting = new EzySimpleZoneSetting();
        setting.setName("test");
        when(zone.getSetting()).thenReturn(setting);
        EzyAppContext appContext = mock(EzyAppContext.class);
        when(zoneContext.getAppContexts()).thenReturn(Lists.newArrayList(appContext));
        doThrow(new IllegalStateException("server maintain")).when(appContext).handleEvent(any(), any());
        
        EzyZoneBroadcastEventImpl cmd = new EzyZoneBroadcastEventImpl(zoneContext);
        EzyServerReadyEvent event = new EzySimpleServerReadyEvent();
        cmd.fire(EzyEventType.SERVER_READY, event, true);
    }
    
    @Test
    public void firePluginEventNoCatchExceptionCaseTest() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzySimpleZoneSetting setting = new EzySimpleZoneSetting();
        setting.setName("test");
        when(zone.getSetting()).thenReturn(setting);
        EzyPluginContext pluginContext = mock(EzyPluginContext.class);
        when(zoneContext.getPluginContexts()).thenReturn(Lists.newArrayList(pluginContext));
        
        EzyZoneBroadcastEventImpl cmd = new EzyZoneBroadcastEventImpl(zoneContext);
        EzyServerReadyEvent event = new EzySimpleServerReadyEvent();
        cmd.fire(EzyEventType.SERVER_READY, event, false);
    }
    
    @Test
    public void firePluginEventCatchExceptionCaseTest() {
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzySimpleZoneSetting setting = new EzySimpleZoneSetting();
        setting.setName("test");
        when(zone.getSetting()).thenReturn(setting);
        EzyPluginContext pluginContext = mock(EzyPluginContext.class);
        when(zoneContext.getPluginContexts()).thenReturn(Lists.newArrayList(pluginContext));
        doThrow(new IllegalStateException("server maintain")).when(pluginContext).handleEvent(any(), any());
        
        EzyZoneBroadcastEventImpl cmd = new EzyZoneBroadcastEventImpl(zoneContext);
        EzyServerReadyEvent event = new EzySimpleServerReadyEvent();
        cmd.fire(EzyEventType.SERVER_READY, event, true);
    }
    
}
