package com.tvd12.ezyfoxserver.nio.testing.builder;

import com.tvd12.ezyfox.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyResponseApiAware;
import com.tvd12.ezyfoxserver.api.EzySecureProxyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyStreamingApiAware;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyNioServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.nio.socket.EzySecureSocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.ezyfoxserver.ssl.*;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import java.util.Collections;

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

    @Test
    @SuppressWarnings("rawtypes")
    public void newSocketDataReceiverBuilderBuildSslTest() {
        // given
        EzyNioServerBootstrapBuilderImpl instance =
            new EzyNioServerBootstrapBuilderImpl();

        EzyServer server = mock(EzySimpleServer.class);
        EzySettings settings = mock(EzySettings.class);
        when(server.getSettings()).thenReturn(settings);

        EzyEventControllers eventControllers = mock(EzyEventControllers.class);
        EzyEventController eventController = mock(EzyEventController.class);
        when(eventControllers.getControllers(EzyEventType.SERVER_INITIALIZING))
            .thenReturn(Collections.singletonList(eventController));
        when(server.getEventControllers()).thenReturn(eventControllers);

        EzySessionManager sessionManager = mock(EzyNioSessionManagerImpl.class);
        when(server.getSessionManager()).thenReturn(sessionManager);

        EzyLoggerSetting loggerSetting = mock(EzyLoggerSetting.class);
        when(settings.getLogger()).thenReturn(loggerSetting);

        EzyLoggerSetting.EzyIgnoredCommandsSetting ignoredCommandsSetting =
            mock(EzyLoggerSetting.EzyIgnoredCommandsSetting.class);
        when(ignoredCommandsSetting.getCommands()).thenReturn(emptySet());
        when(loggerSetting.getIgnoredCommands()).thenReturn(ignoredCommandsSetting);

        EzySocketSetting socketSetting = mock(EzySocketSetting.class);
        when(settings.getSocket()).thenReturn(socketSetting);
        when(socketSetting.isCertificationSslActive()).thenReturn(true);

        EzyThreadPoolSizeSetting threadPoolSizeSetting =
            mock(EzyThreadPoolSizeSetting.class);
        when(settings.getThreadPoolSize()).thenReturn(threadPoolSizeSetting);
        when(threadPoolSizeSetting.getStatistics()).thenReturn(1);

        EzyWebSocketSetting websocketSetting = mock(EzyWebSocketSetting.class);
        when(websocketSetting.isActive()).thenReturn(true);
        when(websocketSetting.isSslActive()).thenReturn(true);
        when(websocketSetting.getCodecCreator())
            .thenReturn(ExCodecCreator.class.getName());
        EzySslConfigSetting sslConfigSetting = mock(EzySslConfigSetting.class);
        when(sslConfigSetting.getContextFactoryBuilder())
            .thenReturn(ExEzySslContextFactoryBuilder.class.getName());
        when(sslConfigSetting.getFile()).thenReturn("ssl-config.properties");
        when(sslConfigSetting.getLoader())
            .thenReturn(ExEzySslConfigLoader.class.getName());
        when(websocketSetting.getSslConfig()).thenReturn(sslConfigSetting);
        when(websocketSetting.getMaxFrameSize()).thenReturn(512);
        when(settings.getWebsocket()).thenReturn(websocketSetting);

        EzyHttpSetting httpSetting = mock(EzyHttpSetting.class);
        when(settings.getHttp()).thenReturn(httpSetting);

        EzyUdpSetting udpSetting = mock(EzyUdpSetting.class);
        when(settings.getUdp()).thenReturn(udpSetting);

        EzyStreamingSetting streamingSetting = mock(EzyStreamingSetting.class);
        when(settings.getStreaming()).thenReturn(streamingSetting);

        EzyConfig config = mock(EzyConfig.class);
        when(config.getEzyfoxHome()).thenReturn("");
        when(server.getConfig()).thenReturn(config);

        instance.server(server);

        // when
        EzyServerBootstrap bootstrap = instance.build();
        try {
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // then
        verify(server, times(23)).getSettings();
        verify(server, times(1)).getStatistics();
        verify(server, times(3)).getSessionManager();
        verify(server, times(2)).getEventControllers();
        verify(server, times(3)).getSessionManager();
        verify(server, times(2)).getConfig();
        verify(((EzyResponseApiAware) server), times(1)).setResponseApi(any());
        verify(((EzyStreamingApiAware) server), times(1)).setStreamingApi(any());
        verifyNoMoreInteractions(server);

        verify(settings, times(5)).getSocket();
        verify(settings, times(1)).getLogger();
        verify(settings, times(1)).getZoneIds();
        verify(settings, times(6)).getThreadPoolSize();
        verify(settings, times(5)).getWebsocket();
        verify(settings, times(2)).getSessionManagement();
        verify(settings, times(1)).getHttp();
        verify(settings, times(1)).getUdp();
        verify(settings, times(1)).getStreaming();
        verify(settings, times(1)).getZoneNames();
        verifyNoMoreInteractions(settings);

        verify(loggerSetting, times(1)).getIgnoredCommands();
        verifyNoMoreInteractions(loggerSetting);

        verify(ignoredCommandsSetting, times(1)).getCommands();
        verifyNoMoreInteractions(ignoredCommandsSetting);

        verify(socketSetting, times(3)).isCertificationSslActive();
        verify(socketSetting, times(2)).isActive();
        verifyNoMoreInteractions(socketSetting);

        verify(threadPoolSizeSetting, times(1)).getStatistics();
        verify(threadPoolSizeSetting, times(1)).getSocketDataReceiver();
        verify(threadPoolSizeSetting, times(1)).getSystemRequestHandler();
        verify(threadPoolSizeSetting, times(1)).getExtensionRequestHandler();
        verify(threadPoolSizeSetting, times(1)).getSocketDisconnectionHandler();
        verify(threadPoolSizeSetting, times(1)).getSocketUserRemovalHandler();
        verifyNoMoreInteractions(threadPoolSizeSetting);

        verify(websocketSetting, times(2)).isActive();
        verify(websocketSetting, times(2)).isSslActive();
        verify(websocketSetting, times(1)).getCodecCreator();
        verify(websocketSetting, times(4)).getMaxFrameSize();
        verify(websocketSetting, times(1)).isManagementEnable();
        verify(websocketSetting, times(2)).getSslPort();
        verify(websocketSetting, times(1)).getPort();
        verify(websocketSetting, times(2)).getAddress();
        verify(websocketSetting, times(1)).getSslConfig();
        verify(websocketSetting, times(1)).getWriterThreadPoolSize();
        verifyNoMoreInteractions(websocketSetting);
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

    public static class ExEzySslConfigLoader
        implements EzySslConfigLoader {

        @Override
        public EzySslConfig load(String s) {
            return new EzySimpleSslConfig();
        }
    }

    public static class ExEzySslContextFactoryBuilder
        implements EzySslContextFactoryBuilder {

        @Override
        public EzySslContextFactory build() {
            return new ExEzySslContextFactory();
        }
    }

    public static class ExEzySslContextFactory
        implements EzySslContextFactory {

        @Override
        public SSLContext newSslContext(
            EzySslConfig config
        ) throws Exception {
            return SSLContext.getDefault();
        }
    }
}
