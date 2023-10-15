package com.tvd12.ezyfoxserver.nio.testing.builder;

import com.tvd12.ezyfox.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzySecureProxyResponseApi;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyNioServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.nio.socket.EzySecureSocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.setting.EzyLoggerSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import static java.util.Collections.emptySet;
import static org.mockito.Mockito.*;

public class EzyNioServerBootstrapBuilderImplTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleConfig config = new EzySimpleConfig();
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.getSocket().setCodecCreator(ExCodecCreator.class.getName());
        settings.getWebsocket().setCodecCreator(ExCodecCreator.class.getName());
        EzySimpleServer server = new EzySimpleServer();
        server.setConfig(config);
        server.setSettings(settings);
        EzyNioServerBootstrapBuilderImpl builder = new EzyNioServerBootstrapBuilderImpl();
        builder.server(server);
        builder.build();
    }

    @Test
    public void newSocketDataReceiverBuilderSslTest() {
        // given
        EzyNioServerBootstrapBuilderImpl instance =
            new EzyNioServerBootstrapBuilderImpl();

        EzyServer server = mock(EzySimpleServer.class);
        EzySettings settings = mock(EzySettings.class);
        when(server.getSettings()).thenReturn(settings);

        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);

        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting =
            mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(ignoredCommandsSetting.getCommands()).thenReturn(emptySet());
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);

        EzySocketSetting socketSetting = mock(EzySocketSetting.class);
        when(settings.getSocket()).thenReturn(socketSetting);
        when(socketSetting.isCertificationSslActive()).thenReturn(true);

        instance.server(server);

        // when
        EzySocketDataReceiver.Builder actual = MethodInvoker.create()
            .object(instance)
            .method("newSocketDataReceiverBuilder")
            .invoke(EzySocketDataReceiver.Builder.class);

        // then
        Asserts.assertEqualsType(actual, EzySecureSocketDataReceiver.Builder.class);

        verify(server, times(3)).getSettings();
        verifyNoMoreInteractions(server);

        verify(settings, times(1)).getSocket();
        verify(settings, times(1)).getLogger();
        verify(settings, times(1)).getZoneIds();
        verifyNoMoreInteractions(settings);

        verify(loggerSetting, times(1)).getIgnoredCommands();
        verifyNoMoreInteractions(loggerSetting);

        verify(ignoredCommandsSetting, times(1)).getCommands();
        verifyNoMoreInteractions(ignoredCommandsSetting);

        verify(socketSetting, times(1)).isCertificationSslActive();
        verifyNoMoreInteractions(socketSetting);
    }

    @Test
    public void newResponseApiSslCase() {
        // given
        EzyNioServerBootstrapBuilderImpl instance =
            new EzyNioServerBootstrapBuilderImpl();

        EzyServer server = mock(EzySimpleServer.class);
        EzySettings settings = mock(EzySettings.class);
        when(server.getSettings()).thenReturn(settings);

        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);

        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting =
            mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(ignoredCommandsSetting.getCommands()).thenReturn(emptySet());
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);

        EzySocketSetting socketSetting = mock(EzySocketSetting.class);
        when(settings.getSocket()).thenReturn(socketSetting);
        when(socketSetting.isCertificationSslActive()).thenReturn(true);

        instance.server(server);

        // when
        EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
        EzyResponseApi actual = MethodInvoker.create()
            .object(instance)
            .method("newResponseApi")
            .param(EzyCodecFactory.class, codecFactory)
            .invoke(EzyResponseApi.class);

        // then
        Asserts.assertEqualsType(actual, EzySecureProxyResponseApi.class);

        verify(server, times(3)).getSettings();
        verifyNoMoreInteractions(server);

        verify(settings, times(1)).getSocket();
        verify(settings, times(1)).getLogger();
        verify(settings, times(1)).getZoneIds();
        verifyNoMoreInteractions(settings);

        verify(loggerSetting, times(1)).getIgnoredCommands();
        verifyNoMoreInteractions(loggerSetting);

        verify(ignoredCommandsSetting, times(1)).getCommands();
        verifyNoMoreInteractions(ignoredCommandsSetting);

        verify(socketSetting, times(1)).isCertificationSslActive();
        verifyNoMoreInteractions(socketSetting);

        verify(codecFactory, times(1)).newEncoder(EzyConnectionType.SOCKET);
        verify(codecFactory, times(1)).newEncoder(EzyConnectionType.WEBSOCKET);
        verifyNoMoreInteractions(codecFactory);
    }

    public static class ExCodecCreator implements EzyCodecCreator {

        @Override
        public Object newEncoder() {
            return null;
        }

        @Override
        public Object newDecoder(int maxRequestSize) {
            return null;
        }
    }
}
