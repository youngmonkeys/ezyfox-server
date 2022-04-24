package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyAccessAppController;
import com.tvd12.ezyfoxserver.delegate.EzySimpleAppUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserAccessAppEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleUserAccessedAppEvent;
import com.tvd12.ezyfoxserver.exception.EzyAccessAppException;
import com.tvd12.ezyfoxserver.exception.EzyMaxUserException;
import com.tvd12.ezyfoxserver.request.EzySimpleAccessAppRequest;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyAppUserManagerImpl;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.concurrent.locks.ReentrantLock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class EzyAccessAppControllerTest extends BaseTest {

    @Test
    public void accessAppSuccessfully() {
        // given
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);

        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);

        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("test");
        when(app.getSetting()).thenReturn(appSetting);
        when(appContext.getApp()).thenReturn(app);

        EzySimpleAppUserDelegate userDelegate = new EzySimpleAppUserDelegate();
        userDelegate.setAppContext(appContext);

        EzyAppUserManager appUserManager = EzyAppUserManagerImpl.builder()
            .appName("test")
            .userDelegate(userDelegate)
            .build();
        when(app.getUserManager()).thenReturn(appUserManager);
        when(zoneContext.getAppContext("test")).thenReturn(appContext);

        EzySimpleAccessAppRequest request = newRequest(1);

        EzyAccessAppController underTest = new EzyAccessAppController();

        // when
        underTest.handle(serverContext, request);

        // then
        verify(appContext, times(1)).handleEvent(
            eq(EzyEventType.USER_ACCESS_APP),
            any(EzySimpleUserAccessAppEvent.class)
        );
        verify(appContext, times(1)).handleEvent(
            eq(EzyEventType.USER_ACCESSED_APP),
            any(EzySimpleUserAccessedAppEvent.class)
        );
        verify(serverContext, times(1)).getZoneContext(1);
        verify(zoneContext, times(1)).getAppContext("test");
        verify(appContext, times(1)).getApp();
        verify(app, times(1)).getSetting();
        verify(app, times(1)).getUserManager();
        verify(serverContext, times(1)).send(
            any(EzyResponse.class),
            any(EzySession.class),
            any(boolean.class)
        );
    }

    @Test
    public void accessAppSuccessfullyBut2Times() {
        // given
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);

        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);

        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("test");
        when(app.getSetting()).thenReturn(appSetting);
        when(appContext.getApp()).thenReturn(app);

        EzySimpleAppUserDelegate userDelegate = new EzySimpleAppUserDelegate();
        userDelegate.setAppContext(appContext);

        EzyAppUserManager appUserManager = EzyAppUserManagerImpl.builder()
            .appName("test")
            .userDelegate(userDelegate)
            .build();
        when(app.getUserManager()).thenReturn(appUserManager);
        when(zoneContext.getAppContext("test")).thenReturn(appContext);

        EzySimpleAccessAppRequest request = newRequest(1);

        EzyAccessAppController underTest = new EzyAccessAppController();

        // when
        underTest.handle(serverContext, request);
        underTest.handle(serverContext, request);

        // then
        verify(appContext, times(2)).handleEvent(
            eq(EzyEventType.USER_ACCESS_APP),
            any(EzySimpleUserAccessAppEvent.class)
        );
        verify(appContext, times(1)).handleEvent(
            eq(EzyEventType.USER_ACCESSED_APP),
            any(EzySimpleUserAccessedAppEvent.class)
        );
        verify(serverContext, times(2)).getZoneContext(1);
        verify(zoneContext, times(2)).getAppContext("test");
        verify(appContext, times(2)).getApp();
        verify(app, times(2)).getSetting();
        verify(app, times(2)).getUserManager();
        verify(serverContext, times(2)).send(
            any(EzyResponse.class),
            any(EzySession.class),
            any(boolean.class)
        );
    }

    @Test
    public void accessAppFailedDueToMaxUser() {
        // given
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);

        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);

        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("test");
        when(app.getSetting()).thenReturn(appSetting);
        when(appContext.getApp()).thenReturn(app);

        EzySimpleAppUserDelegate userDelegate = new EzySimpleAppUserDelegate();
        userDelegate.setAppContext(appContext);

        EzyAppUserManager appUserManager = EzyAppUserManagerImpl.builder()
            .maxUsers(1)
            .appName("test")
            .userDelegate(userDelegate)
            .build();
        when(app.getUserManager()).thenReturn(appUserManager);
        when(zoneContext.getAppContext("test")).thenReturn(appContext);

        EzySimpleAccessAppRequest request1 = newRequest(1);
        EzySimpleAccessAppRequest request2 = newRequest(2);

        EzyAccessAppController underTest = new EzyAccessAppController();

        // when
        underTest.handle(serverContext, request1);
        Throwable e = Asserts.assertThrows(() ->
            underTest.handle(serverContext, request2)
        );

        // then
        Asserts.assertEqualsType(e, EzyAccessAppException.class);
        Asserts.assertEqualsType(e.getCause(), EzyMaxUserException.class);

        verify(appContext, times(1)).handleEvent(
            eq(EzyEventType.USER_ACCESS_APP),
            any(EzySimpleUserAccessAppEvent.class)
        );
        verify(appContext, times(1)).handleEvent(
            eq(EzyEventType.USER_ACCESSED_APP),
            any(EzySimpleUserAccessedAppEvent.class)
        );
        verify(serverContext, times(2)).getZoneContext(1);
        verify(zoneContext, times(2)).getAppContext("test");
        verify(appContext, times(2)).getApp();
        verify(app, times(2)).getSetting();
        verify(app, times(2)).getUserManager();
        verify(serverContext, times(2)).send( // 1 for success, 1 for failure
            any(EzyResponse.class),
            any(EzySession.class),
            any(boolean.class)
        );
    }

    @Test
    public void accessAppFailedDueToMaxUserAfterAccess() {
        // given
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(serverContext.getZoneContext(1)).thenReturn(zoneContext);

        EzyAppContext appContext = mock(EzyAppContext.class);
        EzyApplication app = mock(EzyApplication.class);

        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("test");
        when(app.getSetting()).thenReturn(appSetting);
        when(appContext.getApp()).thenReturn(app);

        EzySimpleAppUserDelegate userDelegate = new EzySimpleAppUserDelegate();
        userDelegate.setAppContext(appContext);

        EzyAppUserManager appUserManager = mock(EzyAppUserManager.class);
        when(appUserManager.getMaxUsers()).thenReturn(1);

        when(app.getUserManager()).thenReturn(appUserManager);
        when(zoneContext.getAppContext("test")).thenReturn(appContext);

        EzySimpleAccessAppRequest request = newRequest(1);
        EzyUser user = request.getUser();
        when(appUserManager.getLock(user.getName())).thenReturn(new ReentrantLock());
        doThrow(new EzyMaxUserException(1, 1)).when(appUserManager).addUser(user);

        EzyAccessAppController underTest = new EzyAccessAppController();

        // when
        Throwable e = Asserts.assertThrows(() ->
            underTest.handle(serverContext, request)
        );

        // then
        Asserts.assertEqualsType(e, EzyAccessAppException.class);
        Asserts.assertEqualsType(e.getCause(), EzyMaxUserException.class);

        verify(appUserManager, times(1)).containsUser(user);
        verify(appUserManager, times(1)).getUserCount();
        verify(appUserManager, times(1)).getMaxUsers();
        verify(appUserManager, times(1)).getAppName();
        verify(appUserManager, times(1)).getLock(user.getName());
        verify(appUserManager, times(1)).removeLock(user.getName());
        verify(appUserManager, times(1)).addUser(user);
        verify(appContext, times(1)).handleEvent(
            eq(EzyEventType.USER_ACCESS_APP),
            any(EzySimpleUserAccessAppEvent.class)
        );
        verify(serverContext, times(1)).getZoneContext(1);
        verify(zoneContext, times(1)).getAppContext("test");
        verify(appContext, times(1)).getApp();
        verify(app, times(1)).getSetting();
        verify(app, times(1)).getUserManager();
        verify(serverContext, times(1)).send( // 1 for success, 1 for failure
            any(EzyResponse.class),
            any(EzySession.class),
            any(boolean.class)
        );
    }

    protected EzySimpleAccessAppRequest newRequest(int index) {
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzySimpleUser user = new EzySimpleUser();
        user.setName("user" + index);
        user.setZoneId(1);
        EzyArray params = EzyEntityFactory.newArrayBuilder()
            .append("test")
            .build();
        EzySimpleAccessAppRequest request = new EzySimpleAccessAppRequest();
        request.setUser(user);
        request.setSession(session);
        request.deserializeParams(params);
        return request;
    }

}
