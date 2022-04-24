package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfoxserver.command.impl.EzyBroadcastEventImpl;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyServerInitializingEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerInitializingEvent;
import com.tvd12.reflections.util.Lists;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyBroadcastEventImplTest extends BaseTest {

    @Test
    public void test() {
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext1 = mock(EzyZoneContext.class);
        EzyZoneContext zoneContext2 = mock(EzyZoneContext.class);
        doThrow(new IllegalStateException()).when(zoneContext2).handleEvent(any(), any());
        when(serverContext.getZoneContexts()).thenReturn(Lists.newArrayList(zoneContext1, zoneContext2));
        EzyBroadcastEventImpl cmd = new EzyBroadcastEventImpl(serverContext);
        EzyServerInitializingEvent event = new EzySimpleServerInitializingEvent();
        cmd.fire(EzyEventType.SERVER_INITIALIZING, event, true);
        try {
            cmd.fire(EzyEventType.SERVER_INITIALIZING, event, false);
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }
    }
}
