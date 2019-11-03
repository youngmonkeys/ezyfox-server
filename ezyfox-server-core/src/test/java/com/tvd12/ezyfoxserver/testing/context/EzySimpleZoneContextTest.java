package com.tvd12.ezyfoxserver.testing.context;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.function.EzyPredicates;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.command.EzyBroadcastEvent;
import com.tvd12.ezyfoxserver.command.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleZoneContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.event.EzySimpleServerInitializingEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerReadyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleUserAccessAppEvent;
import com.tvd12.ezyfoxserver.event.EzyUserAccessAppEvent;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.test.base.BaseTest;

public class EzySimpleZoneContextTest extends BaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void normalCaseTest() {
        EzyServerContext parent = mock(EzyServerContext.class);
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.setName("test");
        EzySimpleZone zone = new EzySimpleZone();
        zone.setUserManager(mock(EzyZoneUserManager.class));
        zone.setSetting(zoneSetting);
        EzySimpleZoneContext context = new EzySimpleZoneContext();
        context.setZone(zone);
        context.setParent(parent);
        context.init();
        assert context.get(EzyBroadcastEvent.class) != null;
        assert context.get(Void.class) == null;
        context.addCommand(ExCommand.class, () -> new ExCommand());
        assert context.cmd(ExCommand.class) != null;
        assert context.cmd(Void.class) == null;
        context.broadcast(EzyEventType.SERVER_INITIALIZING, new EzySimpleServerInitializingEvent(), true);
        context.broadcastApps(EzyEventType.SERVER_READY, new EzySimpleServerReadyEvent(), true);
        EzySimpleUser user = new EzySimpleUser();
        user.setName("dungtv");
        EzyUserAccessAppEvent accessAppEvent = new EzySimpleUserAccessAppEvent(user);
        context.broadcastApps(EzyEventType.USER_ACCESS_APP, accessAppEvent, "dungtv", true);
        context.broadcastApps(EzyEventType.USER_ACCESS_APP, accessAppEvent, user, true);
        context.broadcastApps(EzyEventType.USER_ACCESS_APP, accessAppEvent, EzyPredicates.ALWAY_TRUE, true);
        assert context.equals(context);
        EzyZoneContext zoneContext2 = mock(EzyZoneContext.class);
        EzySimpleZone zone2 = new EzySimpleZone();
        when(zoneContext2.getZone()).thenReturn(zone2);
        assert !zoneContext2.equals(context);
        System.out.println(context.hashCode() + ", " + context.hashCode());
        assert context.hashCode() == context.hashCode();
        
        EzyResponse response = mock(EzyResponse.class);
        EzySession recipient = spy(EzyAbstractSession.class);
        context.send(response, recipient);
        context.send(response, Lists.newArrayList(recipient));
        context.stream(new byte[0], recipient);
        context.stream(new byte[0], Lists.newArrayList(recipient));
        
        context.destroy();
    }
    
    @Test
    public void equalsCaseTest() {
        EzySimpleZoneContext zoneContext1 = new EzySimpleZoneContext();
        EzySimpleZone zone1 = new EzySimpleZone();
        zoneContext1.setZone(zone1);
        
        EzySimpleZoneContext zoneContext2 = new EzySimpleZoneContext();
        EzySimpleZone zone2 = new EzySimpleZone();
        zoneContext1.setZone(zone2);
        
        assert !zoneContext1.equals(zoneContext2);
    }
    
    public static class ExCommand implements EzyCommand<Boolean> {

        @Override
        public Boolean execute() {
            return Boolean.TRUE;
        }
        
    }
    
}
