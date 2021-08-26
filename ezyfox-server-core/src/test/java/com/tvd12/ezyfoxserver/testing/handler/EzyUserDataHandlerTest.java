package com.tvd12.ezyfoxserver.testing.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.command.EzyCloseSession;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.controller.EzyStreamingController;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.handler.EzyUserDataHandler;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.setting.EzyLoggerSetting;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;

public class EzyUserDataHandlerTest {

	@Test
    public void checkToUnmapUserTest() throws Exception {
    	// given
        MyTestDataHandler sut = createHandler();
        FieldUtil.setFieldValue(sut, "user", null);
        
        // when
        MethodInvoker.create()
        	.object(sut)
        	.method("checkToUnmapUser")
        	.param(EzyConstant.class, mock(EzyConstant.class))
        	.invoke();
        
        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "user"));
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
    
    public static class MyTestDataHandler extends EzyUserDataHandler<EzySession> {

        public MyTestDataHandler(EzyServerContext ctx, EzySession session) {
            super(ctx, session);
        }
    }
}
