package com.tvd12.ezyfoxserver.nio.testing.handler;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.exception.EzyMaxRequestSizeException;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.constant.*;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.entity.EzySimpleSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioDataHandler;
import com.tvd12.ezyfoxserver.nio.testing.handler.EzySimpleNioHandlerGroupTest.ExEzyByteToObjectDecoder;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyNioSessionManagerImpl;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkStats;
import com.tvd12.ezyfoxserver.statistics.EzySessionStats;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tvd12.ezyfoxserver.constant.EzyDisconnectReason.MAX_REQUEST_PER_SECOND;
import static com.tvd12.ezyfoxserver.constant.EzyDisconnectReason.MAX_REQUEST_SIZE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;

public class EzyAbstractHandlerGroupTest extends BaseTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void test() throws Exception {
        EzyStatistics statistics = new EzySimpleStatistics();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleStreamingSetting streaming = settings.getStreaming();
        streaming.setEnable(true);
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzySessionManager sessionManager = EzyNioSessionManagerImpl.builder()
            .maxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond())
            .tokenGenerator(new EzySimpleSessionTokenGenerator())
            .build();
        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        EzyNioSession session = (EzyNioSession) sessionManager.provideSession(channel);
        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = new EzySessionTicketsRequestQueues();

        when(channel.write(any(ByteBuffer.class), anyBoolean())).thenReturn(123456);

        ExHandlerGroup group = (ExHandlerGroup) new ExHandlerGroup.Builder()
            .session(session)
            .decoder(decoder)
            .sessionCount(new AtomicInteger())
            .networkStats((EzyNetworkStats) statistics.getSocketStats().getNetworkStats())
            .sessionStats((EzySessionStats) statistics.getSocketStats().getSessionStats())
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();

        EzyChannel channelX = mock(EzyChannel.class);
        MethodInvoker.create()
            .object(group)
            .method("canWriteBytes")
            .param(EzyChannel.class, null)
            .invoke();
        MethodInvoker.create()
            .object(group)
            .method("canWriteBytes")
            .param(EzyChannel.class, channelX)
            .invoke();

        sessionTicketsRequestQueues = mock(EzySessionTicketsRequestQueues.class);
        when(sessionTicketsRequestQueues.addRequest(any())).thenReturn(false);
        group = (ExHandlerGroup) new ExHandlerGroup.Builder()
            .session(session)
            .decoder(decoder)
            .sessionCount(new AtomicInteger())
            .networkStats((EzyNetworkStats) statistics.getSocketStats().getNetworkStats())
            .sessionStats((EzySessionStats) statistics.getSocketStats().getSessionStats())
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();

        MethodInvoker.create()
            .object(group)
            .method("handleReceivedData")
            .param(Object.class, EzyEntityFactory.newArrayBuilder()
                .append(EzyCommand.PING.getId())
                .append(EzyEntityFactory.EMPTY_OBJECT)
                .build())
            .param(int.class, 100)
            .invoke();

        ((EzyDatagramChannelPoolAware) session).setDatagramChannelPool(new EzyDatagramChannelPool(1));
        ((EzyUdpClientAddressAware) session).setUdpClientAddress(new InetSocketAddress("127.0.0.1", 12348));

        EzyPacket packet = mock(EzyPacket.class);
        when(packet.getData()).thenReturn(new byte[]{1, 2, 3});
        ByteBuffer buffer = ByteBuffer.allocate(100);
        MethodInvoker.create()
            .object(group)
            .method("executeSendingPacket")
            .param(EzyPacket.class, packet)
            .param(Object.class, buffer)
            .invoke();

        MethodInvoker.create()
            .object(group)
            .method("executeSendingPacket")
            .param(EzyPacket.class, packet)
            .param(Object.class, new Object())
            .invoke();

        Asserts.assertNotNull(group.getSession());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void fireChannelInactiveException() throws Exception {
        // given
        ExHandlerGroup sut = newHandlerGroup();

        EzySession session = FieldUtil.getFieldValue(sut, "session");

        EzyNioDataHandler handler = FieldUtil.getFieldValue(sut, "handler");
        EzySessionManager sessionManager = FieldUtil.getFieldValue(handler, "sessionManager");
        doThrow(new RuntimeException()).when(sessionManager).clearSession(session);

        // when
        sut.fireChannelInactive(EzyDisconnectReason.ADMIN_BAN);

        // then
        verify(sessionManager, times(1)).clearSession(session);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void fireExceptionCaughtException() throws Exception {
        // given
        ExHandlerGroup sut = newHandlerGroup();

        EzySession session = FieldUtil.getFieldValue(sut, "session");

        EzyNioDataHandler handler = FieldUtil.getFieldValue(sut, "handler");
        EzySessionManager sessionManager = FieldUtil.getFieldValue(handler, "sessionManager");
        doThrow(new RuntimeException()).when(sessionManager).removeSession(session, MAX_REQUEST_SIZE);

        EzyMaxRequestSizeException exception = new EzyMaxRequestSizeException("just test");

        // when
        sut.fireExceptionCaught(exception);

        // then
        verify(sessionManager, times(1)).removeSession(session, MAX_REQUEST_SIZE);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void hasMaxRequestPerSecondTest() throws Exception {
        // given
        ExHandlerGroup sut = newHandlerGroup();

        EzySession session = FieldUtil.getFieldValue(sut, "session");
        when(session.addReceivedRequests(1)).thenReturn(true);

        EzyNioDataHandler handler = FieldUtil.getFieldValue(sut, "handler");
        EzySessionManager sessionManager = FieldUtil.getFieldValue(handler, "sessionManager");

        EzySimpleMaxRequestPerSecond maxRequestPerSecond =
            FieldUtil.getFieldValue(handler, "maxRequestPerSecond");

        maxRequestPerSecond.setAction(EzyMaxRequestPerSecondAction.DISCONNECT_SESSION);

        EzyArray data = EzyEntityFactory.newArray();

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedData")
            .param(Object.class, data)
            .param(int.class, 100)
            .call();

        // then
        verify(sessionManager, times(1)).removeSession(session, MAX_REQUEST_PER_SECOND);
    }

    @Test
    public void handleReceivedDataNotSuccess() throws Exception {
        // given
        ExHandlerGroup sut = newHandlerGroup();

        EzySession session = FieldUtil.getFieldValue(sut, "session");
        when(session.addReceivedRequests(1)).thenReturn(false);
        when(session.isActivated()).thenReturn(true);

        EzySessionTicketsRequestQueues sessionTicketsRequestQueues =
            FieldUtil.getFieldValue(sut, "sessionTicketsRequestQueues");

        EzyArray data = EzyEntityFactory.newArrayBuilder()
            .append(EzyCommand.APP_ACCESS.getId())
            .append(EzyEntityFactory.newArray())
            .build();

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedData")
            .param(Object.class, data)
            .param(int.class, 100)
            .call();

        // then
        verify(sessionTicketsRequestQueues, times(1)).addRequest(any());
    }

    @Test
    public void executeSendingPacketCanNotWriteBytes() throws Exception {
        // given
        ExHandlerGroup sut = newHandlerGroup();
        EzyPacket packet = mock(EzyPacket.class);
        ByteBuffer writeBuffer = ByteBuffer.wrap(new byte[0]);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("executeSendingPacket")
            .param(EzyPacket.class, packet)
            .param(Object.class, writeBuffer)
            .call();

        // then
        EzySession session = FieldUtil.getFieldValue(sut, "session");
        verify(session, times(3)).getChannel();
    }

    @Test
    public void writeUdpPacketToSocketClientAddress() throws Exception {
        // given
        ExHandlerGroup sut = newHandlerGroup();
        EzyPacket packet = mock(EzyPacket.class);
        ByteBuffer writeBuffer = ByteBuffer.wrap(new byte[0]);

        EzyDatagramChannelPool udpChannelPool = mock(EzyDatagramChannelPool.class);
        EzySession session = FieldUtil.getFieldValue(sut, "session");
        when(session.getDatagramChannelPool()).thenReturn(udpChannelPool);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("writeUdpPacketToSocket")
            .param(EzyPacket.class, packet)
            .param(Object.class, writeBuffer)
            .call();

        // then
        verify(session, times(1)).getDatagramChannelPool();
    }

    @Test
    public void getWriteBufferMaxTest() throws Exception {
        // given
        ExHandlerGroup sut = newHandlerGroup();
        ByteBuffer fixed = ByteBuffer.allocate(1);
        int bytesToWrite = 10;

        // when
        ByteBuffer buffer = MethodInvoker.create()
            .object(sut)
            .method("getWriteBuffer")
            .param(ByteBuffer.class, fixed)
            .param(int.class, bytesToWrite)
            .call();

        // then
        Asserts.assertEquals(bytesToWrite, buffer.capacity());
    }

    @SuppressWarnings("rawtypes")
    private ExHandlerGroup newHandlerGroup() throws Exception {
        EzyStatistics statistics = new EzySimpleStatistics();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleStreamingSetting streaming = settings.getStreaming();
        streaming.setEnable(true);
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);

        EzySimpleSession session = mock(EzySimpleSession.class);

        EzySessionManager sessionManager = mock(EzySessionManager.class);
        server.setSessionManager(sessionManager);

        EzyResponseApi responseApi = mock(EzyResponseApi.class);
        server.setResponseApi(responseApi);

        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");
        EzySessionTicketsRequestQueues sessionTicketsRequestQueues = mock(EzySessionTicketsRequestQueues.class);

        when(channel.write(any(ByteBuffer.class), anyBoolean())).thenReturn(123456);

        return (ExHandlerGroup) new ExHandlerGroup.Builder()
            .session(session)
            .decoder(decoder)
            .sessionCount(new AtomicInteger())
            .networkStats((EzyNetworkStats) statistics.getSocketStats().getNetworkStats())
            .sessionStats((EzySessionStats) statistics.getSocketStats().getSessionStats())
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();
    }

    @Test
    public void executeSendingPacketWithTransportTypeWithUdpOrTcpActualUdp() throws Exception {
        // given
        EzyStatistics statistics = new EzySimpleStatistics();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleStreamingSetting streaming = settings.getStreaming();
        streaming.setEnable(true);
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        EzySimpleConfig config = new EzySimpleConfig();
        server.setConfig(config);

        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        when(channel.write(any(ByteBuffer.class), anyBoolean())).thenReturn(123456);

        EzySimpleSession session = mock(EzySimpleSession.class);
        when(session.getChannel()).thenReturn(channel);

        EzyDatagramChannelPool datagramChannelPool =
            mock(EzyDatagramChannelPool.class);
        when(session.getDatagramChannelPool()).thenReturn(datagramChannelPool);

        InetSocketAddress udpAddress = new InetSocketAddress("127.0.0.1", 12348);
        when(session.getUdpClientAddress()).thenReturn(udpAddress);

        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");

        EzySessionTicketsRequestQueues sessionTicketsRequestQueues =
            mock(EzySessionTicketsRequestQueues.class);
        when(sessionTicketsRequestQueues.addRequest(any())).thenReturn(false);
        ExHandlerGroup group = (ExHandlerGroup) new ExHandlerGroup.Builder()
            .session(session)
            .decoder(decoder)
            .sessionCount(new AtomicInteger())
            .networkStats((EzyNetworkStats) statistics.getSocketStats().getNetworkStats())
            .sessionStats((EzySessionStats) statistics.getSocketStats().getSessionStats())
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();

        EzyPacket packet = mock(EzyPacket.class);
        when(
            packet.getTransportType()
        ).thenReturn(EzyTransportType.UDP_OR_TCP);
        when(packet.getData()).thenReturn(new byte[]{1, 2, 3});
        ByteBuffer buffer = ByteBuffer.allocate(100);

        // when
        MethodInvoker.create()
            .object(group)
            .method("executeSendingPacket")
            .param(EzyPacket.class, packet)
            .param(Object.class, buffer)
            .invoke();

        Asserts.assertNotNull(group.getSession());

        // then
        verify(session, times(3)).getChannel();
        verify(session, times(2)).getDatagramChannelPool();
        verify(session, times(1)).getUdpClientAddress();
    }

    @Test
    public void executeSendingPacketWithTransportTypeWithUdpOrTcpActualTcp() throws Exception {
        // given
        EzyStatistics statistics = new EzySimpleStatistics();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleStreamingSetting streaming = settings.getStreaming();
        streaming.setEnable(true);
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        EzySimpleConfig config = new EzySimpleConfig();
        server.setConfig(config);

        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        when(channel.write(any(ByteBuffer.class), anyBoolean())).thenReturn(123456);

        EzySimpleSession session = mock(EzySimpleSession.class);
        when(session.getChannel()).thenReturn(channel);

        InetSocketAddress udpAddress = new InetSocketAddress("127.0.0.1", 12348);
        when(session.getUdpClientAddress()).thenReturn(udpAddress);

        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");

        EzySessionTicketsRequestQueues sessionTicketsRequestQueues =
            mock(EzySessionTicketsRequestQueues.class);
        when(sessionTicketsRequestQueues.addRequest(any())).thenReturn(false);
        ExHandlerGroup group = (ExHandlerGroup) new ExHandlerGroup.Builder()
            .session(session)
            .decoder(decoder)
            .sessionCount(new AtomicInteger())
            .networkStats((EzyNetworkStats) statistics.getSocketStats().getNetworkStats())
            .sessionStats((EzySessionStats) statistics.getSocketStats().getSessionStats())
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();

        EzyPacket packet = mock(EzyPacket.class);
        when(
            packet.getTransportType()
        ).thenReturn(EzyTransportType.UDP_OR_TCP);
        when(packet.getData()).thenReturn(new byte[]{1, 2, 3});
        ByteBuffer buffer = ByteBuffer.allocate(100);

        // when
        MethodInvoker.create()
            .object(group)
            .method("executeSendingPacket")
            .param(EzyPacket.class, packet)
            .param(Object.class, buffer)
            .invoke();

        Asserts.assertNotNull(group.getSession());

        // then
        verify(session, times(3)).getChannel();
        verify(session, times(1)).getDatagramChannelPool();
        verify(session, times(0)).getUdpClientAddress();
    }

    @Test
    public void addReadAndWrittenBytesFailedTest() throws Exception {
        // given
        EzyStatistics statistics = new EzySimpleStatistics();
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleStreamingSetting streaming = settings.getStreaming();
        streaming.setEnable(true);
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        EzySimpleConfig config = new EzySimpleConfig();
        server.setConfig(config);

        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzyChannel channel = mock(EzyChannel.class);
        when(channel.isConnected()).thenReturn(true);
        when(channel.getConnection()).thenReturn(SocketChannel.open());
        when(channel.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        when(channel.write(any(ByteBuffer.class), anyBoolean())).thenReturn(123456);

        EzySimpleSession session = mock(EzySimpleSession.class);
        when(session.getChannel()).thenReturn(channel);
        doThrow(new RuntimeException("test")).when(session).addReadBytes(any(long.class));
        doThrow(new RuntimeException("test")).when(session).addWrittenBytes(any(long.class));

        InetSocketAddress udpAddress = new InetSocketAddress("127.0.0.1", 12348);
        when(session.getUdpClientAddress()).thenReturn(udpAddress);

        ExEzyByteToObjectDecoder decoder = new ExEzyByteToObjectDecoder();
        ExecutorService statsThreadPool = EzyExecutors.newFixedThreadPool(1, "stats");

        EzySessionTicketsRequestQueues sessionTicketsRequestQueues =
            mock(EzySessionTicketsRequestQueues.class);
        when(sessionTicketsRequestQueues.addRequest(any())).thenReturn(false);
        ExHandlerGroup group = (ExHandlerGroup) new ExHandlerGroup.Builder()
            .session(session)
            .decoder(decoder)
            .sessionCount(new AtomicInteger())
            .networkStats((EzyNetworkStats) statistics.getSocketStats().getNetworkStats())
            .sessionStats((EzySessionStats) statistics.getSocketStats().getSessionStats())
            .serverContext(serverContext)
            .statsThreadPool(statsThreadPool)
            .sessionTicketsRequestQueues(sessionTicketsRequestQueues)
            .build();

        // when
        MethodInvoker.create()
            .object(group)
            .method("addReadBytes")
            .param(int.class, 1)
            .invoke();
        MethodInvoker.create()
            .object(group)
            .method("addWrittenBytes")
            .param(int.class, 1)
            .invoke();

        Asserts.assertNotNull(group.getSession());

        // then
        verify(session, atLeast(1)).addReadBytes(1);
        verify(session, atLeast(1)).addWrittenBytes(1);
    }

    @SuppressWarnings("rawtypes")
    public static class ExHandlerGroup
        extends EzyAbstractHandlerGroup
        implements EzyHandlerGroup {

        protected ExHandlerGroup(Builder builder) {
            super(builder);
        }

        @Override
        protected EzyDestroyable newDecoder(Object decoder) {
            return null;
        }

        public static class Builder extends EzyAbstractHandlerGroup.Builder {

            @Override
            public ExHandlerGroup build() {
                return new ExHandlerGroup(this);
            }

        }

    }
}
