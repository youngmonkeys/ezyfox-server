package com.tvd12.ezyfoxserver.nio.testing;

import com.tvd12.ezyfoxserver.EzyBootstrap;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.nio.EzyNioServerBootstrap;
import com.tvd12.ezyfoxserver.nio.protocol.EzyProtocolServersBootstrap;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolGateway;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolServer;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolServerContext;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolServerCreator;
import com.tvd12.ezyfoxserver.setting.EzyEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleProtocolSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleProtocolsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyServerControllersImpl;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodUtil;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.*;

public class EzyNioServerBootstrapTest extends BaseTest {

    @Test
    public void test() throws Exception {
        SSLContext sslContext = SSLContext.getDefault();
        EzyResponseApi responseApi = mock(EzyResponseApi.class);
        EzyStreamingApi streamingApi = mock(EzyStreamingApi.class);
        EzySocketStreamQueue streamQueue = new EzyBlockingSocketStreamQueue();
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySessionTicketsQueue socketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
        EzySessionTicketsQueue websocketSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();
        EzySocketDisconnectionQueue socketDisconnectionQueue = new EzySocketDisconnectionQueue() {

            final BlockingQueue<EzySocketDisconnection> queue = new LinkedBlockingQueue<>();

            @Override
            public EzySocketDisconnection take() throws InterruptedException {
                return queue.take();
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public void remove(EzySocketDisconnection disconnection) {
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void clear() {
            }

            @Override
            public boolean add(EzySocketDisconnection disconnection) {
                return false;
            }
        };

        EzySimpleConfig config = new EzySimpleConfig();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleStreamingSetting streaming = settings.getStreaming();
        streaming.setEnable(true);
        settings.getUdp().setActive(true);
        EzySimpleServer server = new EzySimpleServer();
        EzyServerControllers serverControllers = EzyServerControllersImpl.builder().build();
        server.setControllers(serverControllers);
        EzyEventControllersSetting eventControllersSetting = new EzySimpleEventControllersSetting();
        EzyEventControllers eventControllers = EzyEventControllersImpl.create(eventControllersSetting);
        server.setEventControllers(eventControllers);
        server.setConfig(config);
        server.setSettings(settings);
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setProperty(EzySocketUserRemovalQueue.class, new EzyBlockingSocketUserRemovalQueue());
        serverContext.setServer(server);
        serverContext.init();

        ExBootstrap localBootstrap = new ExBootstrap(new EzyBootstrap.Builder()
            .context(serverContext));

        EzyNioServerBootstrap bootstrap = new EzyNioServerBootstrap();
        bootstrap.setContext(serverContext);
        bootstrap.setLocalBootstrap(localBootstrap);
        EzySslContextProxy sslContextProxy = new EzySslContextProxy(() ->
            sslContext
        );
        bootstrap.setSslContextProxy(sslContextProxy);
        bootstrap.setResponseApi(responseApi);
        bootstrap.setStreamingApi(streamingApi);
        bootstrap.setStreamQueue(streamQueue);
        bootstrap.setHandlerGroupManager(handlerGroupManager);
        bootstrap.setSocketSessionTicketsQueue(socketSessionTicketsQueue);
        bootstrap.setWebsocketSessionTicketsQueue(websocketSessionTicketsQueue);
        bootstrap.setSocketDisconnectionQueue(socketDisconnectionQueue);
        bootstrap.setSocketSessionTicketsRequestQueues(sessionTicketsRequestQueues);
        bootstrap.start();
        bootstrap.destroy();
        bootstrap.destroy();
    }

    @Test
    public void startSocketServerBootstrapNotActive() {
        // given
        EzySimpleServer server = new EzySimpleServer();
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.getSocket().setActive(false);
        server.setSettings(settings);

        EzyServerContext context = mock(EzyServerContext.class);
        when(context.getServer()).thenReturn(server);

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        sut.setContext(context);

        // when
        MethodUtil.invokeMethod("startSocketServerBootstrap", sut);

        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "socketServerBootstrap"));
    }

    @Test
    public void startUdpServerBootstrapNotActive() {
        // given
        EzySimpleServer server = new EzySimpleServer();
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.getUdp().setActive(false);
        server.setSettings(settings);

        EzyServerContext context = mock(EzyServerContext.class);
        when(context.getServer()).thenReturn(server);

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        sut.setContext(context);

        // when
        MethodUtil.invokeMethod("startUdpServerBootstrap", sut);

        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "udpServerBootstrap"));
        sut.destroy();
    }

    @Test
    public void startWebSocketServerBootstrapNotActive() {
        // given
        EzySimpleServer server = new EzySimpleServer();
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.getWebsocket().setActive(false);
        server.setSettings(settings);

        EzyServerContext context = mock(EzyServerContext.class);
        when(context.getServer()).thenReturn(server);

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        sut.setContext(context);

        // when
        MethodUtil.invokeMethod("startWebSocketServerBootstrap", sut);

        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "websocketServerBootstrap"));
    }

    @Test
    public void startStreamHandlingLoopHandlersNotActive() {
        // given
        EzySimpleServer server = new EzySimpleServer();
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.getStreaming().setEnable(false);
        server.setSettings(settings);

        EzyServerContext context = mock(EzyServerContext.class);
        when(context.getServer()).thenReturn(server);

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        sut.setContext(context);

        // when
        MethodUtil.invokeMethod("startStreamHandlingLoopHandlers", sut);

        // then
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "streamHandlingLoopHandler"));
    }

    @Test
    public void startProtocolServersBootstrapWithProtocolsSettingNull() {
        EzySimpleServer server = new EzySimpleServer();
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setProtocols(null);
        server.setSettings(settings);

        EzyServerContext context = mock(EzyServerContext.class);
        when(context.getServer()).thenReturn(server);

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        sut.setContext(context);

        MethodUtil.invokeMethod("startProtocolServersBootstrap", sut);

        Asserts.assertNull(FieldUtil.getFieldValue(sut, "protocolServersBootstrap"));
    }

    @Test
    public void startProtocolServersBootstrapNotActive() {
        EzySimpleServer server = new EzySimpleServer();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleProtocolSetting protocolSetting = new EzySimpleProtocolSetting();
        protocolSetting.setName("test");
        protocolSetting.setActive(false);
        protocolSetting.setServerCreator(ExProtocolServerCreator.class.getName());
        settings.getProtocols().setItem(protocolSetting);
        server.setSettings(settings);

        EzyServerContext context = mock(EzyServerContext.class);
        when(context.getServer()).thenReturn(server);

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        sut.setContext(context);

        MethodUtil.invokeMethod("startProtocolServersBootstrap", sut);

        Asserts.assertNull(FieldUtil.getFieldValue(sut, "protocolServersBootstrap"));
    }

    @Test
    public void startProtocolServersBootstrapTest() throws Exception {
        EzySimpleServer server = new EzySimpleServer();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleProtocolsSetting protocolsSetting = settings.getProtocols();
        protocolsSetting.setWriterThreadPoolSize(1);
        EzySimpleProtocolSetting protocolSetting = new EzySimpleProtocolSetting();
        protocolSetting.setName("test");
        protocolSetting.setServerCreator(ExProtocolServerCreator.class.getName());
        protocolsSetting.setItem(protocolSetting);
        server.setSettings(settings);

        EzyServerContext context = mock(EzyServerContext.class);
        when(context.getServer()).thenReturn(server);

        EzyProtocolGateway protocolGateway = mock(EzyProtocolGateway.class);
        EzySslContextProxy sslContextProxy = mock(EzySslContextProxy.class);
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySessionTicketsQueue protocolSessionTicketsQueue = new EzyBlockingSessionTicketsQueue();

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        sut.setContext(context);
        sut.setProtocolGateway(protocolGateway);
        sut.setSslContextProxy(sslContextProxy);
        sut.setHandlerGroupManager(handlerGroupManager);
        sut.setProtocolSessionTicketsQueue(protocolSessionTicketsQueue);

        MethodUtil.invokeMethod("startProtocolServersBootstrap", sut);

        EzyProtocolServersBootstrap protocolServersBootstrap =
            FieldUtil.getFieldValue(sut, "protocolServersBootstrap");
        Asserts.assertNotNull(protocolServersBootstrap);
        Asserts.assertEquals(
            FieldUtil.getFieldValue(protocolServersBootstrap, "serverContext"),
            context
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(protocolServersBootstrap, "protocolGateway"),
            protocolGateway
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(protocolServersBootstrap, "sslContextProxy"),
            sslContextProxy
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(protocolServersBootstrap, "handlerGroupManager"),
            handlerGroupManager
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(protocolServersBootstrap, "sessionTicketsQueue"),
            protocolSessionTicketsQueue
        );

        List<EzyProtocolServer> protocolServers =
            FieldUtil.getFieldValue(protocolServersBootstrap, "protocolServers");
        Asserts.assertEquals(protocolServers.size(), 1);
        EzyProtocolServer protocolServer = protocolServers.get(0);
        verify(protocolServer, times(1)).start();

        sut.destroy();

        verify(protocolServer, times(1)).destroy();
        Asserts.assertEmpty(protocolServers);
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "protocolServersBootstrap"));
    }

    @Test
    public void startProtocolServersBootstrapFailed() {
        EzySimpleServer server = new EzySimpleServer();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleProtocolsSetting protocolsSetting = settings.getProtocols();
        protocolsSetting.setWriterThreadPoolSize(-1);
        EzySimpleProtocolSetting protocolSetting = new EzySimpleProtocolSetting();
        protocolSetting.setName("test");
        protocolSetting.setServerCreator(ExProtocolServerCreator.class.getName());
        protocolsSetting.setItem(protocolSetting);
        server.setSettings(settings);

        EzyServerContext context = mock(EzyServerContext.class);
        when(context.getServer()).thenReturn(server);

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        sut.setContext(context);

        Throwable e = Asserts.assertThrows(() ->
            MethodUtil.invokeMethod("startProtocolServersBootstrap", sut)
        );

        Asserts.assertEqualsType(
            e.getCause().getCause(),
            NegativeArraySizeException.class
        );
        EzyProtocolServersBootstrap protocolServersBootstrap =
            FieldUtil.getFieldValue(sut, "protocolServersBootstrap");
        Asserts.assertNotNull(protocolServersBootstrap);
        List<EzyProtocolServer> protocolServers =
            FieldUtil.getFieldValue(protocolServersBootstrap, "protocolServers");
        Asserts.assertEmpty(protocolServers);

        sut.destroy();

        Asserts.assertNull(FieldUtil.getFieldValue(sut, "protocolServersBootstrap"));
    }

    @Test
    public void destroyProtocolServersBootstrap() {
        EzyProtocolServersBootstrap protocolServersBootstrap =
            mock(EzyProtocolServersBootstrap.class);

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        FieldUtil.setFieldValue(sut, "protocolServersBootstrap", protocolServersBootstrap);

        sut.destroy();

        verify(protocolServersBootstrap, times(1)).destroy();
        Asserts.assertNull(FieldUtil.getFieldValue(sut, "protocolServersBootstrap"));
    }

    @Test
    public void destroySocketDataReceiver() {
        // given
        EzySocketDataReceiver dataReceiver = mock(EzySocketDataReceiver.class);

        EzyNioServerBootstrap sut = new EzyNioServerBootstrap();
        sut.setSocketDataReceiver(dataReceiver);

        // when
        sut.destroy();

        // then
        verify(dataReceiver, times(1)).destroy();
    }

    public static class ExBootstrap extends EzyBootstrap {

        protected ExBootstrap(Builder builder) {
            super(builder);
        }

        @Override
        public void start() throws Exception {
        }
    }

    public static class ExProtocolServerCreator implements EzyProtocolServerCreator {

        @Override
        public EzyProtocolServer create(EzyProtocolServerContext context) {
            return mock(EzyProtocolServer.class);
        }
    }
}
