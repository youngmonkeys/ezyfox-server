package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyILoginError;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyLoginController;
import com.tvd12.ezyfoxserver.controller.EzyLoginProcessor;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleUserLoginEvent;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import com.tvd12.ezyfoxserver.exception.EzyMaxUserException;
import com.tvd12.ezyfoxserver.exception.EzyZoneNotFoundException;
import com.tvd12.ezyfoxserver.request.EzyLoginParams;
import com.tvd12.ezyfoxserver.request.EzySimpleLoginRequest;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzySimpleUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;

public class EzyLoginControllerTest extends EzyBaseControllerTest {

    @Test
    public void test() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }

    @Test(expectedExceptions = {EzyLoginErrorException.class})
    public void test1() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData1();
        EzyLoginController controller = new EzyLoginController() {
            @Override
            protected void process(EzyServerContext ctx, EzyZoneContext zoneContext, EzyUserLoginEvent event) {
                throw new EzyLoginErrorException();
            }
        };
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }

    @Test(expectedExceptions = {EzyLoginErrorException.class})
    public void test2() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        EzyLoginController controller = new EzyLoginController() {
            @Override
            protected void firePluginEvent(EzyZoneContext zoneContext, EzyUserLoginEvent event) {
                throw new EzyLoginErrorException();
            }
        };
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }

    @Test
    public void allowGuestLoginTest() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        data.set(1, "");
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }

    @Test(expectedExceptions = EzyLoginErrorException.class)
    public void invalidUsernameTest() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        EzyZoneContext zoneContext = ctx.getZoneContext("example");
        EzyZoneSetting zoneSetting = zoneContext.getZone().getSetting();
        EzySimpleUserManagementSetting userManagementSetting = (EzySimpleUserManagementSetting) zoneSetting.getUserManagement();
        userManagementSetting.setAllowGuestLogin(false);
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        data.set(1, "");
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }

    @Test(expectedExceptions = EzyLoginErrorException.class)
    public void maximumSession1Test() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        EzyZoneContext zoneContext = ctx.getZoneContext("example");
        EzyZoneSetting zoneSetting = zoneContext.getZone().getSetting();
        EzySimpleUserManagementSetting userManagementSetting = (EzySimpleUserManagementSetting) zoneSetting.getUserManagement();
        userManagementSetting.setMaxSessionPerUser(0);
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        data.set(1, "");
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }

    @Test(expectedExceptions = EzyLoginErrorException.class)
    public void maximumSession2Test() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        EzyZoneContext zoneContext = ctx.getZoneContext("example");
        EzyZoneSetting zoneSetting = zoneContext.getZone().getSetting();
        EzySimpleUserManagementSetting userManagementSetting = (EzySimpleUserManagementSetting) zoneSetting.getUserManagement();
        userManagementSetting.setMaxSessionPerUser(2);
        userManagementSetting.setAllowChangeSession(false);
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession(1);
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        data.set(1, "helloworld");
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
        request.setSession(newSession(2));
        controller.handle(ctx, request);
        request.setSession(newSession(3));
        controller.handle(ctx, request);
    }

    @Test(expectedExceptions = EzyLoginErrorException.class)
    public void maximumSession3Test() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        EzyZoneContext zoneContext = ctx.getZoneContext("example");
        EzyZoneSetting zoneSetting = zoneContext.getZone().getSetting();
        EzySimpleUserManagementSetting userManagementSetting = (EzySimpleUserManagementSetting) zoneSetting.getUserManagement();
        userManagementSetting.setMaxSessionPerUser(1);
        userManagementSetting.setAllowChangeSession(false);
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        data.set(1, "helloworld");
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
        controller.handle(ctx, request);
    }

    @Test
    public void processChangeSessionTest() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        EzyZoneContext zoneContext = ctx.getZoneContext("example");
        EzyZoneSetting zoneSetting = zoneContext.getZone().getSetting();
        EzySimpleUserManagementSetting userManagementSetting = (EzySimpleUserManagementSetting) zoneSetting.getUserManagement();
        userManagementSetting.setMaxSessionPerUser(1);
        userManagementSetting.setAllowChangeSession(true);
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        data.set(1, "helloworld");
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
        controller.handle(ctx, request);
    }

    @Test
    public void fireUserAddedEvent0ExceptionCase() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzyLoginProcessor processor = new EzyLoginProcessor(ctx);
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(zoneContext.getZone()).thenReturn(ctx.getZoneContext("example").getZone());
        doThrow(new IllegalStateException())
            .when(zoneContext)
            .broadcastPlugins(any(EzyConstant.class), any(EzyEvent.class), anyBoolean());
        MethodInvoker.create()
            .method("doFireUserAddedEvent")
            .object(processor)
            .param(EzyZoneContext.class, zoneContext)
            .param(EzyEvent.class, mock(EzyEvent.class))
            .invoke();
    }

    @Test(expectedExceptions = EzyZoneNotFoundException.class)
    public void testEzyZoneNotFoundException() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        data.set(0, "not found");
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }

    @SuppressWarnings("rawtypes")
    @Test(expectedExceptions = EzyMaxUserException.class)
    public void testEzyMaxUserExceptionCase() {
        EzyServerContext ctx = mock(EzyServerContext.class);
        EzyStatistics userStats = new EzySimpleStatistics();

        EzySimpleServer server = new EzySimpleServer();
        server.setStatistics(userStats);
        when(ctx.getServer()).thenReturn(server);
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(ctx.getZoneContext("example")).thenReturn(zoneContext);
        EzySimpleZone zone = new EzySimpleZone();
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zone.setSetting(zoneSetting);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = EzyZoneUserManagerImpl.builder()
            .maxUsers(1)
            .build();
        zone.setUserManager(zoneUserManager);

        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);

        data.set(1, "test1");
        request.deserializeParams(data);
        controller.handle(ctx, request);
    }

    @SuppressWarnings("rawtypes")
    @Test(expectedExceptions = IllegalStateException.class)
    public void testExceptionCase() {
        EzyServerContext ctx = mock(EzyServerContext.class);
        doThrow(new IllegalStateException("server maintain"))
            .when(ctx)
            .send(any(EzyResponse.class), any(EzySession.class), any(boolean.class));
        EzyStatistics userStats = new EzySimpleStatistics();

        EzySimpleServer server = new EzySimpleServer();
        server.setStatistics(userStats);
        when(ctx.getServer()).thenReturn(server);
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        when(ctx.getZoneContext("example")).thenReturn(zoneContext);
        EzySimpleZone zone = new EzySimpleZone();
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zone.setSetting(zoneSetting);
        when(zoneContext.getZone()).thenReturn(zone);
        EzyZoneUserManager zoneUserManager = EzyZoneUserManagerImpl.builder()
            .maxUsers(1)
            .build();
        zone.setUserManager(zoneUserManager);

        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        EzyLoginController controller = new EzyLoginController() {
            @Override
            protected void responseLoginError(EzyServerContext ctx, EzySession session, EzyILoginError error) {
            }
        };
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }

    @Test
    public void testSetUserProperties() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        EzyLoginProcessor processor = new EzyLoginProcessor(ctx);
        EzyZoneContext zoneContext = ctx.getZoneContext("example");
        EzyLoginParams params = request.getParams();
        EzySimpleUserLoginEvent event = new EzySimpleUserLoginEvent(
            session,
            params.getZoneName(),
            params.getUsername(),
            params.getPassword(), params.getData());
        event.setUserProperty("dataId", 123L);
        processor.apply(zoneContext, event);
        event.release();
        EzyZoneUserManager userManager = zoneContext.getZone().getUserManager();
        EzyUser user = userManager.getUser("dungtv");
        assert user.getProperty("dataId").equals(123L);
    }

    private EzyArray newLoginData() {
        return newArrayBuilder()
            .append("example")
            .append("dungtv")
            .append("123456")
            .append(newArrayBuilder()
                .append("123456"))
            .build();
    }

    private EzyArray newLoginData1() {
        return newArrayBuilder()
            .append("example")
            .append("dungtv")
            .append("123456")
            .append(newArrayBuilder())
            .build();
    }

    @Override
    protected EzyConstant getCommand() {
        return EzyCommand.LOGIN;
    }

}
