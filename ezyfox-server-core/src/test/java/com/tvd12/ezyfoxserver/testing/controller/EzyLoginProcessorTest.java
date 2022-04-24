package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyLoginProcessor;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyUserStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyLoginProcessorTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void checkUsernameIsNull() {
        // given
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);

        EzySimpleServer server = new EzySimpleServer();
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyStatistics statistics = mock(EzyStatistics.class);
        EzyUserStatistics userStatistics = mock(EzyUserStatistics.class);
        when(statistics.getUserStats()).thenReturn(userStatistics);
        server.setStatistics(statistics);

        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getServer()).thenReturn(server);

        EzyLoginProcessor sut = new EzyLoginProcessor(serverContext);

        EzyUserLoginEvent event = mock(EzyUserLoginEvent.class);

        // when
        Throwable e = Asserts.assertThrows(() -> sut.apply(zoneContext, event));

        // then
        Asserts.assertEquals(EzyLoginErrorException.class, e.getClass());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void applyWithStreamingEnable() {
        // given
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.getStreaming().setEnable(true);

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);

        EzySimpleServer server = new EzySimpleServer();
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyZoneUserManager userManager = EzyZoneUserManagerImpl.builder()
            .build();
        zone.setUserManager(userManager);

        EzyStatistics statistics = mock(EzyStatistics.class);
        EzyUserStatistics userStatistics = mock(EzyUserStatistics.class);
        when(statistics.getUserStats()).thenReturn(userStatistics);
        server.setStatistics(statistics);

        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getServer()).thenReturn(server);

        EzyLoginProcessor sut = new EzyLoginProcessor(serverContext);

        EzySessionDelegate sessionDelegate = mock(EzySessionDelegate.class);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setDelegate(sessionDelegate);


        EzyUserLoginEvent event = mock(EzyUserLoginEvent.class);
        when(event.getUsername()).thenReturn("monkey");
        when(event.getSession()).thenReturn(session);
        when(event.isStreamingEnable()).thenReturn(true);

        // when
        sut.apply(zoneContext, event);

        // then
        Asserts.assertTrue(session.isStreamingEnable());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void applyWithStreamingDisableByEvent() {
        // given
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.getStreaming().setEnable(true);

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);

        EzySimpleServer server = new EzySimpleServer();
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyZoneUserManager userManager = EzyZoneUserManagerImpl.builder()
            .build();
        zone.setUserManager(userManager);

        EzyStatistics statistics = mock(EzyStatistics.class);
        EzyUserStatistics userStatistics = mock(EzyUserStatistics.class);
        when(statistics.getUserStats()).thenReturn(userStatistics);
        server.setStatistics(statistics);

        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getServer()).thenReturn(server);

        EzyLoginProcessor sut = new EzyLoginProcessor(serverContext);

        EzySessionDelegate sessionDelegate = mock(EzySessionDelegate.class);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setDelegate(sessionDelegate);


        EzyUserLoginEvent event = mock(EzyUserLoginEvent.class);
        when(event.getUsername()).thenReturn("monkey");
        when(event.getSession()).thenReturn(session);
        when(event.isStreamingEnable()).thenReturn(false);

        // when
        sut.apply(zoneContext, event);

        // then
        Asserts.assertFalse(session.isStreamingEnable());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void processUserSessionsMaxSessionPerUser() {
        // given
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.getUserManagement().setMaxSessionPerUser(1);

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);

        EzySimpleServer server = new EzySimpleServer();
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyZoneUserManager userManager = EzyZoneUserManagerImpl.builder()
            .build();
        EzyUser user = mock(EzyUser.class);
        when(user.getName()).thenReturn("monkey");
        when(user.getSessionCount()).thenReturn(2);
        userManager.addUser(user);

        zone.setUserManager(userManager);

        EzyStatistics statistics = mock(EzyStatistics.class);
        EzyUserStatistics userStatistics = mock(EzyUserStatistics.class);
        when(statistics.getUserStats()).thenReturn(userStatistics);
        server.setStatistics(statistics);

        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getServer()).thenReturn(server);

        EzyLoginProcessor sut = new EzyLoginProcessor(serverContext);

        EzySessionDelegate sessionDelegate = mock(EzySessionDelegate.class);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setDelegate(sessionDelegate);


        EzyUserLoginEvent event = mock(EzyUserLoginEvent.class);
        when(event.getUsername()).thenReturn("monkey");
        when(event.getSession()).thenReturn(session);

        // when
        Throwable e = Asserts.assertThrows(() -> sut.apply(zoneContext, event));

        // then
        Asserts.assertEquals(EzyLoginErrorException.class, e.getClass());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void processUserSessionsMaxSessionPerUserGreaterThan1() {
        // given
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.getUserManagement().setMaxSessionPerUser(2);

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);

        EzySimpleServer server = new EzySimpleServer();
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyZoneUserManager userManager = EzyZoneUserManagerImpl.builder()
            .build();
        EzyUser user = mock(EzyUser.class);
        when(user.getName()).thenReturn("monkey");
        when(user.getSessionCount()).thenReturn(2);
        userManager.addUser(user);

        zone.setUserManager(userManager);

        EzyStatistics statistics = mock(EzyStatistics.class);
        EzyUserStatistics userStatistics = mock(EzyUserStatistics.class);
        when(statistics.getUserStats()).thenReturn(userStatistics);
        server.setStatistics(statistics);

        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getServer()).thenReturn(server);

        EzyLoginProcessor sut = new EzyLoginProcessor(serverContext);

        EzySessionDelegate sessionDelegate = mock(EzySessionDelegate.class);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setDelegate(sessionDelegate);


        EzyUserLoginEvent event = mock(EzyUserLoginEvent.class);
        when(event.getUsername()).thenReturn("monkey");
        when(event.getSession()).thenReturn(session);

        // when
        Throwable e = Asserts.assertThrows(() -> sut.apply(zoneContext, event));

        // then
        Asserts.assertEquals(EzyLoginErrorException.class, e.getClass());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void processUserSessionsButNotAllowToChangeSession() {
        // given
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.getUserManagement().setMaxSessionPerUser(1);
        zoneSetting.getUserManagement().setAllowChangeSession(false);

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);

        EzySimpleServer server = new EzySimpleServer();
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyZoneUserManager userManager = EzyZoneUserManagerImpl.builder()
            .build();
        EzyUser user = mock(EzyUser.class);
        when(user.getName()).thenReturn("monkey");
        when(user.getSessionCount()).thenReturn(1);
        userManager.addUser(user);

        zone.setUserManager(userManager);

        EzyStatistics statistics = mock(EzyStatistics.class);
        EzyUserStatistics userStatistics = mock(EzyUserStatistics.class);
        when(statistics.getUserStats()).thenReturn(userStatistics);
        server.setStatistics(statistics);

        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getServer()).thenReturn(server);

        EzyLoginProcessor sut = new EzyLoginProcessor(serverContext);

        EzySessionDelegate sessionDelegate = mock(EzySessionDelegate.class);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setDelegate(sessionDelegate);


        EzyUserLoginEvent event = mock(EzyUserLoginEvent.class);
        when(event.getUsername()).thenReturn("monkey");
        when(event.getSession()).thenReturn(session);

        // when
        Throwable e = Asserts.assertThrows(() -> sut.apply(zoneContext, event));

        // then
        Asserts.assertEquals(EzyLoginErrorException.class, e.getClass());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void processUserSessionsOk() {
        // given
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.getUserManagement().setMaxSessionPerUser(1);
        zoneSetting.getUserManagement().setAllowChangeSession(true);

        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(zone);

        EzySimpleServer server = new EzySimpleServer();
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyZoneUserManager userManager = EzyZoneUserManagerImpl.builder()
            .build();
        EzyUser user = mock(EzyUser.class);
        when(user.getName()).thenReturn("monkey");
        when(user.getSessionCount()).thenReturn(1);
        userManager.addUser(user);

        zone.setUserManager(userManager);

        EzyStatistics statistics = mock(EzyStatistics.class);
        EzyUserStatistics userStatistics = mock(EzyUserStatistics.class);
        when(statistics.getUserStats()).thenReturn(userStatistics);
        server.setStatistics(statistics);

        EzyServerContext serverContext = mock(EzyServerContext.class);
        when(serverContext.getServer()).thenReturn(server);

        EzyLoginProcessor sut = new EzyLoginProcessor(serverContext);

        EzySessionDelegate sessionDelegate = mock(EzySessionDelegate.class);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setDelegate(sessionDelegate);


        EzyUserLoginEvent event = mock(EzyUserLoginEvent.class);
        when(event.getUsername()).thenReturn("monkey");
        when(event.getSession()).thenReturn(session);

        // when
        sut.apply(zoneContext, event);

        // then
        Asserts.assertTrue(session.isLoggedIn());
    }
}
