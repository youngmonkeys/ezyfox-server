package com.tvd12.ezyfoxserver.nio.testing.protocol;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.protocol.EzyProtocolServersBootstrap;
import com.tvd12.ezyfoxserver.nio.protocol.EzyProtocolWritingLoopHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolGateway;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolServer;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolServerContext;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolServerCreator;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleProtocolSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleProtocolsSetting;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketEventHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoop;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketWriter;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

public class EzyProtocolServersBootstrapTest {

    private static final Queue<EzyProtocolServer> PROTOCOL_SERVERS =
        new ConcurrentLinkedQueue<>();
    private static final List<EzyProtocolServerContext> PROTOCOL_SERVER_CONTEXTS =
        Collections.synchronizedList(new ArrayList<>());

    private EzyServer server;
    private EzyServerContext serverContext;
    private EzyProtocolGateway protocolGateway;
    private EzySslContextProxy sslContextProxy;
    private EzyHandlerGroupManager handlerGroupManager;
    private EzySessionTicketsQueue sessionTicketsQueue;
    private EzySimpleProtocolsSetting protocolsSetting;
    private EzyProtocolServersBootstrap instance;

    @BeforeMethod
    public void setup() {
        server = mock(EzyServer.class);
        serverContext = mock(EzyServerContext.class);
        protocolGateway = mock(EzyProtocolGateway.class);
        sslContextProxy = mock(EzySslContextProxy.class);
        handlerGroupManager = mock(EzyHandlerGroupManager.class);
        sessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
        protocolsSetting = new EzySimpleProtocolsSetting();
        protocolsSetting.setWriterThreadPoolSize(1);

        EzySettings settings = mock(EzySettings.class);
        when(serverContext.getServer()).thenReturn(server);
        when(server.getSettings()).thenReturn(settings);
        when(settings.getProtocols()).thenReturn(protocolsSetting);

        instance = EzyProtocolServersBootstrap.builder()
            .serverContext(serverContext)
            .protocolGateway(protocolGateway)
            .sslContextProxy(sslContextProxy)
            .handlerGroupManager(handlerGroupManager)
            .sessionTicketsQueue(sessionTicketsQueue)
            .build();
    }

    @AfterMethod
    public void tearDown() {
        instance.destroy();
        PROTOCOL_SERVERS.clear();
        PROTOCOL_SERVER_CONTEXTS.clear();
    }

    @Test
    public void startTest() throws Exception {
        EzySimpleProtocolSetting setting = newProtocolSetting(
            "test",
            TestProtocolServerCreator.class.getName()
        );
        protocolsSetting.setItem(setting);

        EzyProtocolServer protocolServer = mock(EzyProtocolServer.class);
        PROTOCOL_SERVERS.add(protocolServer);

        instance.start();

        verify(protocolServer, times(1)).start();
        verify(server, times(1)).getClassLoader();

        List<EzyProtocolServer> protocolServers = getProtocolServers();
        Asserts.assertEquals(protocolServers.size(), 1);
        Asserts.assertEquals(protocolServers.get(0), protocolServer);

        Asserts.assertEquals(PROTOCOL_SERVER_CONTEXTS.size(), 1);
        EzyProtocolServerContext context = PROTOCOL_SERVER_CONTEXTS.get(0);
        Asserts.assertEquals(context.getSetting(), setting);
        Asserts.assertEquals(context.getGateway(), protocolGateway);
        Asserts.assertEquals(context.getServerContext(), serverContext);
        Asserts.assertEquals(context.getSslContextProxy(), sslContextProxy);
    }

    @Test
    public void startWritingLoopHandlerTest() throws Exception {
        instance.start();

        EzySocketEventLoopHandler writingLoopHandler =
            FieldUtil.getFieldValue(instance, "writingLoopHandler");
        Asserts.assertEqualsType(
            writingLoopHandler,
            EzyProtocolWritingLoopHandler.class
        );

        EzySocketEventLoop eventLoop =
            FieldUtil.getFieldValue(writingLoopHandler, "eventLoop");
        Boolean active = FieldUtil.getFieldValue(eventLoop, "active");
        Asserts.assertTrue(active);
        Asserts.assertEquals(
            FieldUtil.getFieldValue(eventLoop, "threadName"),
            "protocol-writer"
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(eventLoop, "threadPoolSize"),
            1
        );

        Supplier<EzySocketEventHandler> eventHandlerSupplier =
            FieldUtil.getFieldValue(writingLoopHandler, "eventHandlerSupplier");
        EzySocketEventHandler eventHandler = eventHandlerSupplier.get();
        Asserts.assertEqualsType(eventHandler, EzySocketWriter.class);
        Asserts.assertEquals(
            FieldUtil.getFieldValue(eventHandler, "writerGroupFetcher"),
            handlerGroupManager
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(eventHandler, "sessionTicketsQueue"),
            sessionTicketsQueue
        );
        Asserts.assertEmpty(getProtocolServers());
    }

    @Test
    public void startWithInactiveProtocolTest() throws Exception {
        EzySimpleProtocolSetting setting = newProtocolSetting(
            "test",
            TestProtocolServerCreator.class.getName()
        );
        setting.setActive(false);
        protocolsSetting.setItem(setting);

        EzyProtocolServer protocolServer = mock(EzyProtocolServer.class);
        PROTOCOL_SERVERS.add(protocolServer);

        instance.start();

        verify(protocolServer, never()).start();
        verify(server, never()).getClassLoader();
        Asserts.assertEmpty(PROTOCOL_SERVER_CONTEXTS);
        Asserts.assertEmpty(getProtocolServers());
    }

    @Test
    public void startWithClassLoaderTest() throws Exception {
        String serverCreator = TestProtocolServerCreator.class.getName();
        protocolsSetting.setItem(newProtocolSetting("test", serverCreator));

        TestClassLoader classLoader = new TestClassLoader(
            getClass().getClassLoader()
        );
        when(server.getClassLoader()).thenReturn(classLoader);

        EzyProtocolServer protocolServer = mock(EzyProtocolServer.class);
        PROTOCOL_SERVERS.add(protocolServer);

        instance.start();

        verify(protocolServer, times(1)).start();
        verify(server, times(1)).getClassLoader();
        Asserts.assertTrue(classLoader.loadedClassNames.contains(serverCreator));

        List<EzyProtocolServer> protocolServers = getProtocolServers();
        Asserts.assertEquals(protocolServers.size(), 1);
        Asserts.assertEquals(protocolServers.get(0), protocolServer);
    }

    @Test
    public void startWithServerCreatorIsNullTest() throws Exception {
        EzySimpleProtocolSetting nullCreatorSetting = newProtocolSetting(
            "null-creator",
            null
        );
        EzySimpleProtocolSetting setting = newProtocolSetting(
            "test",
            TestProtocolServerCreator.class.getName()
        );
        protocolsSetting.setItem(nullCreatorSetting);
        protocolsSetting.setItem(setting);

        EzyProtocolServer protocolServer = mock(EzyProtocolServer.class);
        PROTOCOL_SERVERS.add(protocolServer);

        instance.start();

        verify(protocolServer, times(1)).start();

        Asserts.assertEquals(PROTOCOL_SERVER_CONTEXTS.size(), 1);
        Asserts.assertEquals(
            PROTOCOL_SERVER_CONTEXTS.get(0).getSetting(),
            setting
        );

        List<EzyProtocolServer> protocolServers = getProtocolServers();
        Asserts.assertEquals(protocolServers.size(), 1);
        Asserts.assertEquals(protocolServers.get(0), protocolServer);
    }

    @Test
    public void startWithServerCreatorNotFoundTest() throws Exception {
        EzySimpleProtocolSetting notFoundCreatorSetting = newProtocolSetting(
            "not-found-creator",
            "com.tvd12.ezyfoxserver.nio.testing.protocol.NotFoundServerCreator"
        );
        EzySimpleProtocolSetting setting = newProtocolSetting(
            "test",
            TestProtocolServerCreator.class.getName()
        );
        protocolsSetting.setItem(notFoundCreatorSetting);
        protocolsSetting.setItem(setting);

        EzyProtocolServer protocolServer = mock(EzyProtocolServer.class);
        PROTOCOL_SERVERS.add(protocolServer);

        instance.start();

        verify(protocolServer, times(1)).start();
        verify(server, times(2)).getClassLoader();

        Asserts.assertEquals(PROTOCOL_SERVER_CONTEXTS.size(), 1);
        Asserts.assertEquals(
            PROTOCOL_SERVER_CONTEXTS.get(0).getSetting(),
            setting
        );

        List<EzyProtocolServer> protocolServers = getProtocolServers();
        Asserts.assertEquals(protocolServers.size(), 1);
        Asserts.assertEquals(protocolServers.get(0), protocolServer);
    }

    @Test
    public void startWithProtocolServerStartFailedTest() throws Exception {
        String serverCreator = TestProtocolServerCreator.class.getName();
        protocolsSetting.setItem(newProtocolSetting("failed", serverCreator));
        protocolsSetting.setItem(newProtocolSetting("test", serverCreator));

        EzyProtocolServer failedProtocolServer = mock(EzyProtocolServer.class);
        doThrow(new IllegalStateException("test"))
            .when(failedProtocolServer)
            .start();
        EzyProtocolServer protocolServer = mock(EzyProtocolServer.class);
        PROTOCOL_SERVERS.add(failedProtocolServer);
        PROTOCOL_SERVERS.add(protocolServer);

        instance.start();
        instance.destroy();

        verify(failedProtocolServer, times(1)).start();
        verify(failedProtocolServer, never()).destroy();
        verify(protocolServer, times(1)).start();
        verify(protocolServer, times(1)).destroy();
        Asserts.assertEquals(PROTOCOL_SERVER_CONTEXTS.size(), 2);
    }

    @Test
    public void startThenDestroyTest() throws Exception {
        protocolsSetting.setItem(
            newProtocolSetting("test", TestProtocolServerCreator.class.getName())
        );

        EzyProtocolServer protocolServer = mock(EzyProtocolServer.class);
        PROTOCOL_SERVERS.add(protocolServer);

        instance.start();
        EzySocketEventLoopHandler writingLoopHandler =
            FieldUtil.getFieldValue(instance, "writingLoopHandler");
        EzySocketEventLoop eventLoop =
            FieldUtil.getFieldValue(writingLoopHandler, "eventLoop");

        instance.destroy();

        verify(protocolServer, times(1)).destroy();
        Asserts.assertEmpty(getProtocolServers());

        Boolean active = FieldUtil.getFieldValue(eventLoop, "active");
        Asserts.assertFalse(active);
    }

    @Test
    public void destroyTest() {
        EzyProtocolServer protocolServer1 = mock(EzyProtocolServer.class);
        doThrow(new IllegalStateException("test"))
            .when(protocolServer1)
            .destroy();
        EzyProtocolServer protocolServer2 = mock(EzyProtocolServer.class);

        List<EzyProtocolServer> protocolServers = getProtocolServers();
        protocolServers.add(protocolServer1);
        protocolServers.add(protocolServer2);

        EzySocketEventLoopHandler writingLoopHandler =
            mock(EzySocketEventLoopHandler.class);
        doThrow(new IllegalStateException("test"))
            .when(writingLoopHandler)
            .destroy();
        FieldUtil.setFieldValue(instance, "writingLoopHandler", writingLoopHandler);

        instance.destroy();

        verify(protocolServer1, times(1)).destroy();
        verify(protocolServer2, times(1)).destroy();
        verify(writingLoopHandler, times(1)).destroy();
        Asserts.assertEmpty(protocolServers);
    }

    @Test
    public void destroyWithoutStartTest() {
        instance.destroy();

        Asserts.assertNull(FieldUtil.getFieldValue(instance, "writingLoopHandler"));
        Asserts.assertEmpty(getProtocolServers());
    }

    private List<EzyProtocolServer> getProtocolServers() {
        return FieldUtil.getFieldValue(instance, "protocolServers");
    }

    private static EzySimpleProtocolSetting newProtocolSetting(
        String name,
        String serverCreator
    ) {
        EzySimpleProtocolSetting setting = new EzySimpleProtocolSetting();
        setting.setName(name);
        setting.setServerCreator(serverCreator);
        return setting;
    }

    public static class TestProtocolServerCreator
        implements EzyProtocolServerCreator {

        @Override
        public EzyProtocolServer create(EzyProtocolServerContext context) {
            PROTOCOL_SERVER_CONTEXTS.add(context);
            return PROTOCOL_SERVERS.poll();
        }
    }

    private static class TestClassLoader extends ClassLoader {

        private final List<String> loadedClassNames =
            Collections.synchronizedList(new ArrayList<>());

        TestClassLoader(ClassLoader parent) {
            super(parent);
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            loadedClassNames.add(name);
            return super.loadClass(name);
        }
    }
}
