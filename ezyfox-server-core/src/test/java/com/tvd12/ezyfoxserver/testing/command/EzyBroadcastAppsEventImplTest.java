package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.command.impl.EzyBroadcastAppsEventImpl;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerInitializingEvent;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.reflections.util.Lists;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class EzyBroadcastAppsEventImplTest extends BaseTest {

    @Test
    public void test() {
        EzyEvent event2 = new EzySimpleServerInitializingEvent();
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        List<EzyAppContext> appContexts = Lists.newArrayList(
            newAppContext("1", event2),
            newAppContext("2", event2),
            newAppContext("3", event2)
        );
        when(zoneContext.getAppContexts()).thenReturn(appContexts);
        EzyZone zone = mock(EzyZone.class);
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.setName("test");
        when(zone.getSetting()).thenReturn(zoneSetting);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyBroadcastAppsEventImpl cmd = new EzyBroadcastAppsEventImpl(zoneContext);
        EzyEvent event = new EzySimpleServerInitializingEvent();
        cmd.fire(EzyEventType.SERVER_INITIALIZING, event, true);
        cmd.fire(EzyEventType.SERVER_INITIALIZING, event, false);
        EzySimpleUser user = new EzySimpleUser();
        user.setName("user1");
        cmd.fire(EzyEventType.SERVER_INITIALIZING, event, user, true);
        cmd.fire(EzyEventType.SERVER_INITIALIZING, event, "user1", true);
        cmd.fire(EzyEventType.SERVER_INITIALIZING, event2, true);
    }

    private EzyAppContext newAppContext(String appName, EzyEvent event2) {
        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);
        when(appContext.getApp()).thenReturn(app);
        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        setting.setName(appName);
        when(app.getSetting()).thenReturn(setting);
        EzyAppUserManager appUserManager = EzyAppUserManagerImpl.builder()
            .appName(appName)
            .maxUsers(99)
            .build();
        when(app.getUserManager()).thenReturn(appUserManager);
        EzySimpleUser user = new EzySimpleUser();
        user.setName("user" + appName);
        appUserManager.addUser(user);
        doThrow(new IllegalStateException()).when(appContext).handleEvent(EzyEventType.SERVER_INITIALIZING, event2);
        return appContext;
    }
}
