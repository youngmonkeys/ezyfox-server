package com.tvd12.ezyfoxserver.testing.handler;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.exception.EzyMaxRequestSizeException;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.command.EzyCloseSession;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.controller.EzyStreamingController;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.handler.EzySimpleDataHandler;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyLoggerSetting;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.reflect.MethodUtil;

public class EzySimpleDataHandlerTest extends BaseCoreTest {
    
    @SuppressWarnings("rawtypes")
    @Test
    public void normalCase() throws Exception {
        int zoneId = 1;
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);
        when(serverContext.getZoneContext(zoneId)).thenReturn(zoneContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);
        EzyServer server = mock(EzyServer.class);
        when(serverContext.getServer()).thenReturn(server);
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        EzyInterceptor streamingInteceptor = mock(EzyInterceptor.class);
        when(controllers.getStreamingInterceptor()).thenReturn(streamingInteceptor);
        EzyStreamingController streamingController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamingController);
        EzyInterceptor loginInteceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.LOGIN)).thenReturn(loginInteceptor);
        EzyController loginController = mock(EzyController.class);
        when(controllers.getController(EzyCommand.LOGIN)).thenReturn(loginController);
        when(server.getControllers()).thenReturn(controllers);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        EzyCloseSession closeSession = mock(EzyCloseSession.class);
        when(serverContext.get(EzyCloseSession.class)).thenReturn(closeSession);
        EzySettings settings = mock(EzySettings.class);
        when(settings.isDebug()).thenReturn(true);
        when(server.getSettings()).thenReturn(settings);
        EzySessionManagementSetting sessionManagementSetting = mock(EzySessionManagementSetting.class);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);
        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting = mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(ignoredCommandsSetting.getCommands()).thenReturn(new HashSet<>());
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getValue()).thenReturn(3);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond()).thenReturn(maxRequestPerSecond);
        
        MyTestDataHandler handler = new MyTestDataHandler(serverContext, session);
        
        when(session.getDelegate()).thenReturn(handler);
        when(session.isActivated()).thenReturn(true);
        
        EzyArray loginData = EzyEntityFactory.newArrayBuilder()
                .append("zone")
                .append("username")
                .append("password")
                .append(EzyEntityFactory.newObject())
                .build();
        EzyArray requestData = EzyEntityFactory.newArrayBuilder()
                .append(EzyCommand.LOGIN.getId())
                .append(loginData)
                .build();
        handler.dataReceived(EzyCommand.LOGIN, requestData);
        
        EzySimpleUser user = new EzySimpleUser();
        user.addSession(session);
        user.setZoneId(zoneId);
        handler.onSessionLoggedIn(user);
        
        handler.streamingReceived(new byte[] {1, 2, 3});
        
        EzyArray accessAppData = EzyEntityFactory.newArrayBuilder()
                .append("app")
                .append(EzyEntityFactory.newObject())
                .build();
        EzyArray requestData2 = EzyEntityFactory.newArrayBuilder()
                .append(EzyCommand.APP_ACCESS.getId())
                .append(accessAppData)
                .build();
        handler.dataReceived(EzyCommand.APP_ACCESS, requestData2);
        
        handler.dataReceived(EzyCommand.LOGIN, requestData);
        handler.dataReceived(EzyCommand.LOGIN, requestData);

        handler.exceptionCaught(new EzyMaxRequestSizeException(1024, 512));

        handler.channelInactive(EzyDisconnectReason.ADMIN_BAN);
        handler.channelInactive(EzyDisconnectReason.ADMIN_BAN);
        handler.destroy();
        
        System.out.println(handler);
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void checkMaxRequestPerSecondCase() throws Exception {
        int zoneId = 1;
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);
        when(serverContext.getZoneContext(zoneId)).thenReturn(zoneContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);
        EzyServer server = mock(EzyServer.class);
        when(serverContext.getServer()).thenReturn(server);
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        EzyInterceptor streamingInteceptor = mock(EzyInterceptor.class);
        when(controllers.getStreamingInterceptor()).thenReturn(streamingInteceptor);
        EzyStreamingController streamingController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamingController);
        EzyInterceptor loginInteceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.LOGIN)).thenReturn(loginInteceptor);
        EzyController loginController = mock(EzyController.class);
        when(controllers.getController(EzyCommand.LOGIN)).thenReturn(loginController);
        when(server.getControllers()).thenReturn(controllers);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        EzyCloseSession closeSession = mock(EzyCloseSession.class);
        when(serverContext.get(EzyCloseSession.class)).thenReturn(closeSession);
        EzySettings settings = mock(EzySettings.class);
        when(settings.isDebug()).thenReturn(true);
        when(server.getSettings()).thenReturn(settings);
        EzySessionManagementSetting sessionManagementSetting = mock(EzySessionManagementSetting.class);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);
        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting = mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(ignoredCommandsSetting.getCommands()).thenReturn(new HashSet<>());
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getValue()).thenReturn(3);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond()).thenReturn(maxRequestPerSecond);
        
        MyTestDataHandler handler = new MyTestDataHandler(serverContext, session);
        
        when(session.getDelegate()).thenReturn(handler);
        when(session.isActivated()).thenReturn(true);
        
        EzyArray loginData = EzyEntityFactory.newArrayBuilder()
                .append("zone")
                .append("username")
                .append("password")
                .append(EzyEntityFactory.newObject())
                .build();
        EzyArray requestData = EzyEntityFactory.newArrayBuilder()
                .append(EzyCommand.LOGIN.getId())
                .append(loginData)
                .build();
        handler.dataReceived(EzyCommand.LOGIN, requestData);
        
        EzySimpleUser user = new EzySimpleUser();
        user.addSession(session);
        user.setZoneId(zoneId);
        handler.onSessionLoggedIn(user);
        
        handler.streamingReceived(new byte[] {1, 2, 3});
        
        EzyArray accessAppData = EzyEntityFactory.newArrayBuilder()
                .append("app")
                .append(EzyEntityFactory.newObject())
                .build();
        EzyArray requestData2 = EzyEntityFactory.newArrayBuilder()
                .append(EzyCommand.APP_ACCESS.getId())
                .append(accessAppData)
                .build();
        handler.dataReceived(EzyCommand.APP_ACCESS, requestData2);
        
        handler.dataReceived(EzyCommand.LOGIN, requestData);
        Thread.sleep(1200);
        handler.dataReceived(EzyCommand.LOGIN, requestData);
    }
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void handleReceivedStreaming0ExceptionCase() throws Exception {
        int zoneId = 1;
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);
        when(serverContext.getZoneContext(zoneId)).thenReturn(zoneContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);
        EzyServer server = mock(EzyServer.class);
        when(serverContext.getServer()).thenReturn(server);
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        EzyInterceptor streamingInteceptor = mock(EzyInterceptor.class);
        doThrow(new IllegalArgumentException("server maintain")).when(streamingInteceptor).intercept(any(), any());
        when(controllers.getStreamingInterceptor()).thenReturn(streamingInteceptor);
        EzyStreamingController streamingController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamingController);
        EzyInterceptor loginInteceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.LOGIN)).thenReturn(loginInteceptor);
        EzyController loginController = mock(EzyController.class);
        when(controllers.getController(EzyCommand.LOGIN)).thenReturn(loginController);
        when(server.getControllers()).thenReturn(controllers);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        EzyCloseSession closeSession = mock(EzyCloseSession.class);
        when(serverContext.get(EzyCloseSession.class)).thenReturn(closeSession);
        EzySettings settings = mock(EzySettings.class);
        when(settings.isDebug()).thenReturn(true);
        when(server.getSettings()).thenReturn(settings);
        EzySessionManagementSetting sessionManagementSetting = mock(EzySessionManagementSetting.class);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);
        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting = mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(ignoredCommandsSetting.getCommands()).thenReturn(new HashSet<>());
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getValue()).thenReturn(3);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond()).thenReturn(maxRequestPerSecond);
        
        MyTestDataHandler handler = new MyTestDataHandler(serverContext, session);
        
        when(session.getDelegate()).thenReturn(handler);
        when(session.isActivated()).thenReturn(true);
        
        EzySimpleUser user = new EzySimpleUser();
        user.addSession(session);
        user.setZoneId(zoneId);
        handler.onSessionLoggedIn(user);
        
        handler.streamingReceived(new byte[] {1, 2, 3});
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void handleRequestExceptionCase1() throws Exception {
        int zoneId = 1;
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);
        when(serverContext.getZoneContext(zoneId)).thenReturn(zoneContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);
        EzyServer server = mock(EzyServer.class);
        when(serverContext.getServer()).thenReturn(server);
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        EzyInterceptor streamingInteceptor = mock(EzyInterceptor.class);
        when(controllers.getStreamingInterceptor()).thenReturn(streamingInteceptor);
        EzyStreamingController streamingController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamingController);
        EzyInterceptor loginInteceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.LOGIN)).thenReturn(loginInteceptor);
        EzyController loginController = mock(EzyController.class);
        when(controllers.getController(EzyCommand.LOGIN)).thenReturn(loginController);
        when(server.getControllers()).thenReturn(controllers);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        EzyCloseSession closeSession = mock(EzyCloseSession.class);
        when(serverContext.get(EzyCloseSession.class)).thenReturn(closeSession);
        EzySettings settings = mock(EzySettings.class);
        when(settings.isDebug()).thenReturn(true);
        when(server.getSettings()).thenReturn(settings);
        EzySessionManagementSetting sessionManagementSetting = mock(EzySessionManagementSetting.class);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);
        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting = mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(ignoredCommandsSetting.getCommands()).thenReturn(new HashSet<>());
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getValue()).thenReturn(3);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond()).thenReturn(maxRequestPerSecond);
        
        MyTestDataHandler handler = new MyTestDataHandler(serverContext, session);
        
        when(session.getDelegate()).thenReturn(handler);
        when(session.isActivated()).thenReturn(true);
        
        EzyArray loginData = EzyEntityFactory.newArrayBuilder()
                .append("zone")
                .append("username")
                .append("password")
                .append(EzyEntityFactory.newObject())
                .build();
        EzyArray requestData = EzyEntityFactory.newArrayBuilder()
                .append(EzyCommand.LOGIN.getId())
                .append(loginData)
                .build();
        
        doThrow(new IllegalStateException("server maintain")).when(loginInteceptor).intercept(any(), any());
        FieldUtil.setFieldValue(handler, "context", null);

        handler.dataReceived(EzyCommand.LOGIN, requestData);

    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void handleRequestExceptionCase2() throws Exception {
        int zoneId = 1;
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);
        when(serverContext.getZoneContext(zoneId)).thenReturn(zoneContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);
        EzyServer server = mock(EzyServer.class);
        when(serverContext.getServer()).thenReturn(server);
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        EzyInterceptor streamingInteceptor = mock(EzyInterceptor.class);
        when(controllers.getStreamingInterceptor()).thenReturn(streamingInteceptor);
        EzyStreamingController streamingController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamingController);
        EzyInterceptor loginInteceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.LOGIN)).thenReturn(loginInteceptor);
        EzyController loginController = mock(EzyController.class);
        when(controllers.getController(EzyCommand.LOGIN)).thenReturn(loginController);
        when(server.getControllers()).thenReturn(controllers);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        EzyCloseSession closeSession = mock(EzyCloseSession.class);
        when(serverContext.get(EzyCloseSession.class)).thenReturn(closeSession);
        EzySettings settings = mock(EzySettings.class);
        when(settings.isDebug()).thenReturn(true);
        when(server.getSettings()).thenReturn(settings);
        EzySessionManagementSetting sessionManagementSetting = mock(EzySessionManagementSetting.class);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);
        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting = mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(ignoredCommandsSetting.getCommands()).thenReturn(new HashSet<>());
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getValue()).thenReturn(3);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond()).thenReturn(maxRequestPerSecond);
        
        MyTestDataHandler handler = new MyTestDataHandler(serverContext, session);
        
        when(session.getDelegate()).thenReturn(handler);
        when(session.isActivated()).thenReturn(true);
        
        EzyArray loginData = EzyEntityFactory.newArrayBuilder()
                .append("zone")
                .append("username")
                .append("password")
                .append(EzyEntityFactory.newObject())
                .build();
        
        doThrow(new IllegalStateException("server maintain")).when(loginInteceptor).intercept(any(), any());
        FieldUtil.setFieldValue(handler, "context", null);
        FieldUtil.setFieldValue(handler, "active", false);
        
        MethodInvoker.create()
            .object(handler)
            .method("handleRequest")
            .param(EzyConstant.class, EzyCommand.LOGIN)
            .param(EzyArray.class, loginData)
            .invoke();
    }
    
    @SuppressWarnings({ "rawtypes"})
    @Test
    public void notifyAppsSessionRemoved0Case() throws Exception {
        int zoneId = 1;
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);
        when(serverContext.getZoneContext(zoneId)).thenReturn(zoneContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);
        EzyServer server = mock(EzyServer.class);
        when(serverContext.getServer()).thenReturn(server);
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        EzyInterceptor streamingInteceptor = mock(EzyInterceptor.class);
        when(controllers.getStreamingInterceptor()).thenReturn(streamingInteceptor);
        EzyStreamingController streamingController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamingController);
        EzyInterceptor loginInteceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.LOGIN)).thenReturn(loginInteceptor);
        EzyController loginController = mock(EzyController.class);
        when(controllers.getController(EzyCommand.LOGIN)).thenReturn(loginController);
        when(server.getControllers()).thenReturn(controllers);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        EzyCloseSession closeSession = mock(EzyCloseSession.class);
        when(serverContext.get(EzyCloseSession.class)).thenReturn(closeSession);
        EzySettings settings = mock(EzySettings.class);
        when(settings.isDebug()).thenReturn(true);
        when(server.getSettings()).thenReturn(settings);
        EzySessionManagementSetting sessionManagementSetting = mock(EzySessionManagementSetting.class);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);
        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting = mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(ignoredCommandsSetting.getCommands()).thenReturn(new HashSet<>());
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getValue()).thenReturn(3);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond()).thenReturn(maxRequestPerSecond);
        
        MyTestDataHandler handler = new MyTestDataHandler(serverContext, session);
        
        when(session.getDelegate()).thenReturn(handler);
        when(session.isActivated()).thenReturn(true);
        
        FieldUtil.setFieldValue(handler, "zoneContext", zoneContext);
        doThrow(new IllegalArgumentException("notifyAppsSessionRemoved0Case")).when(zoneContext).broadcastApps(any(EzyConstant.class), any(EzyEvent.class), any(EzyUser.class), anyBoolean());
        MethodInvoker.create()
            .object(handler)
            .method("notifyAppsSessionRemoved0")
            .param(EzyEvent.class, mock(EzyEvent.class))
            .invoke();
        
    }
    
    @SuppressWarnings({ "rawtypes"})
    @Test
    public void notifyPluginsSessionRemovedCase() throws Exception {
        int zoneId = 1;
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);
        when(serverContext.getZoneContext(zoneId)).thenReturn(zoneContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);
        EzyServer server = mock(EzyServer.class);
        when(serverContext.getServer()).thenReturn(server);
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        EzyInterceptor streamingInteceptor = mock(EzyInterceptor.class);
        when(controllers.getStreamingInterceptor()).thenReturn(streamingInteceptor);
        EzyStreamingController streamingController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamingController);
        EzyInterceptor loginInteceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.LOGIN)).thenReturn(loginInteceptor);
        EzyController loginController = mock(EzyController.class);
        when(controllers.getController(EzyCommand.LOGIN)).thenReturn(loginController);
        when(server.getControllers()).thenReturn(controllers);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        EzyCloseSession closeSession = mock(EzyCloseSession.class);
        when(serverContext.get(EzyCloseSession.class)).thenReturn(closeSession);
        EzySettings settings = mock(EzySettings.class);
        when(settings.isDebug()).thenReturn(true);
        when(server.getSettings()).thenReturn(settings);
        EzySessionManagementSetting sessionManagementSetting = mock(EzySessionManagementSetting.class);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);
        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting = mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(ignoredCommandsSetting.getCommands()).thenReturn(new HashSet<>());
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getValue()).thenReturn(3);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond()).thenReturn(maxRequestPerSecond);
        
        MyTestDataHandler handler = new MyTestDataHandler(serverContext, session);
        
        when(session.getDelegate()).thenReturn(handler);
        when(session.isActivated()).thenReturn(true);
        
        FieldUtil.setFieldValue(handler, "zoneContext", zoneContext);
        doThrow(new IllegalArgumentException("notifyPluginsSessionRemovedCase")).when(zoneContext).broadcastPlugins(any(EzyConstant.class), any(EzyEvent.class), anyBoolean());
        MethodInvoker.create()
            .object(handler)
            .method("notifyPluginsSessionRemoved")
            .param(EzyEvent.class, mock(EzyEvent.class))
            .invoke();
        
    }
    
    @SuppressWarnings({ "rawtypes"})
    @Test
    public void closeSessionExceptionCase() throws Exception {
        int zoneId = 1;
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);
        when(serverContext.getZoneContext(zoneId)).thenReturn(zoneContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);
        EzyServer server = mock(EzyServer.class);
        when(serverContext.getServer()).thenReturn(server);
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        EzyInterceptor streamingInteceptor = mock(EzyInterceptor.class);
        when(controllers.getStreamingInterceptor()).thenReturn(streamingInteceptor);
        EzyStreamingController streamingController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamingController);
        EzyInterceptor loginInteceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.LOGIN)).thenReturn(loginInteceptor);
        EzyController loginController = mock(EzyController.class);
        when(controllers.getController(EzyCommand.LOGIN)).thenReturn(loginController);
        when(server.getControllers()).thenReturn(controllers);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        EzyCloseSession closeSession = mock(EzyCloseSession.class);
        when(serverContext.get(EzyCloseSession.class)).thenReturn(closeSession);
        EzySettings settings = mock(EzySettings.class);
        when(settings.isDebug()).thenReturn(true);
        when(server.getSettings()).thenReturn(settings);
        EzySessionManagementSetting sessionManagementSetting = mock(EzySessionManagementSetting.class);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);
        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting = mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(ignoredCommandsSetting.getCommands()).thenReturn(new HashSet<>());
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getValue()).thenReturn(3);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond()).thenReturn(maxRequestPerSecond);
        
        MyTestDataHandler handler = new MyTestDataHandler(serverContext, session);
        
        when(session.getDelegate()).thenReturn(handler);
        when(session.isActivated()).thenReturn(true);
        
        doThrow(new IllegalArgumentException("closeSessionExceptionCase")).when(closeSession).close(any(EzySession.class), any(EzyConstant.class));
        MethodInvoker.create()
            .object(handler)
            .method("closeSession")
            .param(EzyConstant.class, EzyDisconnectReason.ADMIN_BAN)
            .invoke();
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void processMaxRequestPerSecondDisconnect() {
        // given
        EzySession session = spy(EzyAbstractSession.class);
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyServer server = mock(EzyServer.class);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        when(serverContext.getServer()).thenReturn(server);


        EzySessionManagementSetting sessionManagementSetting =
                mock(EzySessionManagementSetting.class);
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond())
            .thenReturn(maxRequestPerSecond);

        EzySettings settings = mock(EzySettings.class);
        when(server.getSettings()).thenReturn(settings);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);

        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting =
                mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(ignoredCommandsSetting.getCommands()).thenReturn(Collections.emptySet());
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(settings.getLogger()).thenReturn(loggerSetting);

        MyTestDataHandler sut = new MyTestDataHandler(serverContext, session);

        // when
        sut.processMaxRequestPerSecond();

        // then
        verify(sessionManager, times(1)).removeSession(session, EzyDisconnectReason.MAX_REQUEST_PER_SECOND);;
    }
    
    
    @Test
    public void dataReceivedValidateStateFalse() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        sut.destroy();
        
        EzyArray msg = EzyEntityFactory.newArray();
        
        // when
        sut.dataReceived(EzyCommand.APP_ACCESS, msg);
        
        // then
        Asserts.assertEquals(false, FieldUtil.getFieldValue(sut, "active"));
    }
    
    @Test
    public void dataReceivedValidateSessionIsNull() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        FieldUtil.setFieldValue(sut, "session", null);
        
        EzyArray msg = EzyEntityFactory.newArray();
        
        // when
        sut.dataReceived(EzyCommand.APP_ACCESS, msg);
        
        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "session"));
    }
    
    @Test
    public void dataReceivedValidateSessionInActive() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        EzySession session = FieldUtil.getFieldValue(sut, "session");
        session.setActivated(false);
        
        EzyArray msg = EzyEntityFactory.newArray();
        
        // when
        sut.dataReceived(EzyCommand.APP_ACCESS, msg);
        
        // then
        Asserts.assertFalse(session.isActivated());
    }
    
    @Test
    public void streamingReceivedValidateStateFalse() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        sut.destroy();
        
        // when
        sut.streamingReceived(new byte[0]);
        
        // then
        Asserts.assertEquals(false, FieldUtil.getFieldValue(sut, "active"));
    }
    
    @Test
    public void streamingReceivedValidateSessionIsNull() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        FieldUtil.setFieldValue(sut, "session", null);
        
        // when
        sut.streamingReceived(new byte[0]);
        
        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "session"));
    }
    
    @Test
    public void processMaxRequestPerSecondActionDiffDisconnection() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                FieldUtil.getFieldValue(sut, "maxRequestPerSecond");
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DROP_REQUEST);
        
        // when
        sut.processMaxRequestPerSecond();
        
        // then
        verify(maxRequestPerSecond, times(1)).getAction();
    }
    
    @Test
    public void processMaxRequestPerSecondWithSessionManagerIsNull() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        FieldUtil.setFieldValue(sut, "sessionManager", null);
        
        // when
        sut.processMaxRequestPerSecond();
        
        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "sessionManager"));
    }
    
    @Test
    public void debugLogReceivedDataIsNotDebug() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        EzySettings settings = FieldUtil.getFieldValue(sut, "settings");
        when(settings.isDebug()).thenReturn(false);
        
        // when
        MethodInvoker.create()
            .object(sut)
            .method("debugLogReceivedData")
            .param(EzyConstant.class, EzyCommand.APP_ACCESS)
            .param(EzyArray.class, EzyEntityFactory.EMPTY_ARRAY)
            .call();
        
        // then
        verify(settings, times(1)).isDebug();
    }
    
    @Test
    public void debugLogReceivedDataUnloggableCommandsIsPing() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        EzySettings settings = FieldUtil.getFieldValue(sut, "settings");
        when(settings.isDebug()).thenReturn(true);
        
        // when
        MethodInvoker.create()
            .object(sut)
            .method("debugLogReceivedData")
            .param(EzyConstant.class, EzyCommand.PING)
            .param(EzyArray.class, EzyEntityFactory.EMPTY_ARRAY)
            .call();
        
        // then
        verify(settings, times(1)).isDebug();
    }
    
    @Test
    public void exceptionCaughtHandlerNull() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        Throwable e = new Exception("just test");
        
        // when
        // then
        sut.exceptionCaught(e);
    }
    
    @Test
    public void removeSessionWithSessionManagerIsNull() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        FieldUtil.setFieldValue(sut, "sessionManager", null);
        
        // when
        MethodUtil.invokeMethod("removeSession", sut);
        
        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "sessionManager"));
    }
    
    @Test
    public void notifySessionRemovedZoneContextIsNull() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        FieldUtil.setFieldValue(sut, "zoneContext", null);
        
        // when
        MethodInvoker.create()
            .object(sut)
            .method("notifySessionRemoved")
            .param(EzyConstant.class, EzyDisconnectReason.ADMIN_BAN)
            .invoke();
        
        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "zoneContext"));
    }
    
    @Test
    public void notifyAppsSessionRemovedUserNull() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        FieldUtil.setFieldValue(sut, "user", null);
        
        // when
        MethodInvoker.create()
            .object(sut)
            .method("notifyAppsSessionRemoved")
            .param(EzyEvent.class, mock(EzyEvent.class))
            .invoke();
        
        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "user"));
    }
    
    @Test
    public void responseContextIsNull() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        FieldUtil.setFieldValue(sut, "context", null);
        
        // when
        MethodInvoker.create()
            .object(sut)
            .method("response")
            .param(EzyResponse.class, mock(EzyResponse.class))
            .invoke();
        
        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "context"));
    }
    
    @Test
    public void exceptionCaughtSessionManagerIsNull() throws Exception {
        // given
        MyTestDataHandler sut = createHandler();
        Throwable e = new EzyMaxRequestSizeException("just test");
        FieldUtil.setFieldValue(sut, "sessionManager", null);
        
        // when
        sut.exceptionCaught(e);
        
     // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "sessionManager"));
    }
    
    @SuppressWarnings("rawtypes")
    private MyTestDataHandler createHandler() {
        int zoneId = 1;
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzyZone zone = mock(EzyZone.class);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = mock(EzyZoneUserManager.class);
        when(zone.getUserManager()).thenReturn(zoneUserManager);
        when(serverContext.getZoneContext(zoneId)).thenReturn(zoneContext);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzyChannel channel = mock(EzyChannel.class);
        when(session.getChannel()).thenReturn(channel);
        EzyServer server = mock(EzyServer.class);
        when(serverContext.getServer()).thenReturn(server);
        EzyServerControllers controllers = mock(EzyServerControllers.class);
        EzyInterceptor streamingInteceptor = mock(EzyInterceptor.class);
        when(controllers.getStreamingInterceptor()).thenReturn(streamingInteceptor);
        EzyStreamingController streamingController = mock(EzyStreamingController.class);
        when(controllers.getStreamingController()).thenReturn(streamingController);
        EzyInterceptor loginInteceptor = mock(EzyInterceptor.class);
        when(controllers.getInterceptor(EzyCommand.LOGIN)).thenReturn(loginInteceptor);
        EzyController loginController = mock(EzyController.class);
        when(controllers.getController(EzyCommand.LOGIN)).thenReturn(loginController);
        when(server.getControllers()).thenReturn(controllers);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        when(server.getSessionManager()).thenReturn(sessionManager);
        EzyCloseSession closeSession = mock(EzyCloseSession.class);
        when(serverContext.get(EzyCloseSession.class)).thenReturn(closeSession);
        EzySettings settings = mock(EzySettings.class);
        when(settings.isDebug()).thenReturn(true);
        when(server.getSettings()).thenReturn(settings);
        EzySessionManagementSetting sessionManagementSetting = mock(EzySessionManagementSetting.class);
        when(settings.getSessionManagement()).thenReturn(sessionManagementSetting);
        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);
        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting = mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);
        when(ignoredCommandsSetting.getCommands()).thenReturn(Sets.newHashSet(EzyCommand.PING));
        EzySessionManagementSetting.EzyMaxRequestPerSecond maxRequestPerSecond =
                mock(EzySessionManagementSetting.EzyMaxRequestPerSecond.class);
        when(maxRequestPerSecond.getValue()).thenReturn(3);
        when(maxRequestPerSecond.getAction()).thenReturn(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);
        when(sessionManagementSetting.getSessionMaxRequestPerSecond()).thenReturn(maxRequestPerSecond);
        
        return new MyTestDataHandler(serverContext, session);
    }
    
    public static class MyTestDataHandler extends EzySimpleDataHandler<EzySession> {

        public MyTestDataHandler(EzyServerContext ctx, EzySession session) {
            super(ctx, session);
        }
    }
}
