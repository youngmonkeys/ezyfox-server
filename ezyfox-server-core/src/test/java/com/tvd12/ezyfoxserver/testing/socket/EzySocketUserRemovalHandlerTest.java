package com.tvd12.ezyfoxserver.testing.socket;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyUserEvent;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketUserRemoval;
import com.tvd12.ezyfoxserver.socket.EzySocketUserRemoval;
import com.tvd12.ezyfoxserver.socket.EzySocketUserRemovalHandler;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

public class EzySocketUserRemovalHandlerTest {

    @Test
    public void test() {
        TestBlockingSocketUserRemovalQueue queue = new TestBlockingSocketUserRemovalQueue();
        EzyAppContext appContext1 = mock(EzyAppContext.class);
        EzyAppUserManager userManager1 = mock(EzyAppUserManager.class);
        when(userManager1.containsUser(any(EzyUser.class))).thenReturn(true);
        EzyApplication app1 = mock(EzyApplication.class);
        when(app1.getUserManager()).thenReturn(userManager1);
        when(appContext1.getApp()).thenReturn(app1);
        
        EzyAppContext appContext2 = mock(EzyAppContext.class);
        EzyAppUserManager userManager2 = mock(EzyAppUserManager.class);
        when(userManager2.containsUser(any(EzyUser.class))).thenReturn(false);
        EzyApplication app2 = mock(EzyApplication.class);
        when(app2.getUserManager()).thenReturn(userManager2);
        when(appContext2.getApp()).thenReturn(app2);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getAppContexts()).thenReturn(Lists.newArrayList(appContext1, appContext2));
        EzySimpleUser user = new EzySimpleUser();
        user.setName("test");
        EzySocketUserRemoval item = new EzySimpleSocketUserRemoval(zoneContext, user, EzyUserRemoveReason.EXIT_APP);
        queue.add(item);
        EzySocketUserRemovalHandler handler = new EzySocketUserRemovalHandler();
        handler = new EzySocketUserRemovalHandler(queue);
        handler.handleEvent();
        handler.destroy();
    }
    
    @Test
    public void processUserRemovalQueueInterruptCaseTest() throws Exception {
        TestBlockingSocketUserRemovalQueue queue = new TestBlockingSocketUserRemovalQueue();
        EzySocketUserRemovalHandler handler = new EzySocketUserRemovalHandler(queue);
        AtomicInteger count = new AtomicInteger();
        Thread thread = new Thread(() -> {
            while(count.incrementAndGet() < 1000) {
                handler.handleEvent();
                try {
                    Thread.sleep(100);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(300L);
        thread.interrupt();
    }
    
    @Test
    public void processUserRemovalQueueThrowableCaseTest() {
        TestBlockingSocketUserRemovalQueue queue = new TestBlockingSocketUserRemovalQueue();
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzySimpleUser user = new EzySimpleUser();
        user.setName("test");
        EzySocketUserRemoval item = new EzySimpleSocketUserRemoval(zoneContext, user, EzyUserRemoveReason.EXIT_APP) {
            @Override
            public void release() {
                throw new RuntimeException();
            }
        };
        queue.add(item);
        EzySocketUserRemovalHandler handler = new EzySocketUserRemovalHandler(queue);
        handler.handleEvent();
    }
    
    @Test
    public void notifyUserRemovedToPluginsExceptionCaseTest() {
        TestBlockingSocketUserRemovalQueue queue = new TestBlockingSocketUserRemovalQueue();
        EzyAppContext appContext1 = mock(EzyAppContext.class);
        EzyAppUserManager userManager1 = mock(EzyAppUserManager.class);
        when(userManager1.containsUser(any(EzyUser.class))).thenReturn(true);
        EzyApplication app1 = mock(EzyApplication.class);
        when(app1.getUserManager()).thenReturn(userManager1);
        when(appContext1.getApp()).thenReturn(app1);
        doThrow(new RuntimeException()).when(appContext1).handleEvent(any(EzyConstant.class), any(EzyUserEvent.class));
        
        EzyAppContext appContext2 = mock(EzyAppContext.class);
        EzyAppUserManager userManager2 = mock(EzyAppUserManager.class);
        when(userManager2.containsUser(any(EzyUser.class))).thenReturn(false);
        EzyApplication app2 = mock(EzyApplication.class);
        when(app2.getUserManager()).thenReturn(userManager2);
        when(appContext2.getApp()).thenReturn(app2);
        
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getAppContexts()).thenReturn(Lists.newArrayList(appContext1, appContext2));
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneSetting zoneSetting = mock(EzyZoneSetting.class);
        when(zoneSetting.getName()).thenReturn("test");
        when(zone.getSetting()).thenReturn(zoneSetting);
        doThrow(new RuntimeException()).when(zoneContext).broadcastPlugins(any(EzyConstant.class), any(EzyUserEvent.class), anyBoolean());
        
        EzySimpleUser user = new EzySimpleUser();
        user.setName("test");
        EzySocketUserRemoval item = new EzySimpleSocketUserRemoval(zoneContext, user, EzyUserRemoveReason.EXIT_APP);
        queue.add(item);
        EzySocketUserRemovalHandler handler = new EzySocketUserRemovalHandler(queue);
        handler.handleEvent();
        handler.destroy();
    }
}
