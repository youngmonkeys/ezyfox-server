package com.tvd12.ezyfoxserver.nio.testing.handler;

import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.io.EzyByteBuffers;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.entity.EzySimpleSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzySimpleNioUdpDataHandler;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPool;
import com.tvd12.ezyfoxserver.socket.EzySimpleUdpReceivedPacket;
import com.tvd12.ezyfoxserver.socket.EzyUdpReceivedPacket;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.util.RandomUtil;
import org.eclipse.jetty.websocket.api.Session;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EzySimpleNioUdpDataHandlerTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void test() throws Exception {
        String sessionToken = "12345678";
        long sessionId = 12345L;
        EzySimpleNioUdpDataHandler handler = new EzySimpleNioUdpDataHandler(1);
        int tokenSize = sessionToken.length();
        int messageSize = 0;
        messageSize += 8; // sessionIdSize
        messageSize += 2; // tokenLengthSize
        messageSize += tokenSize; // messageSize
        ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + messageSize);
        byte header = 0;
        header |= 1 << 5;
        buffer.put(header);
        buffer.putShort((short) messageSize);
        buffer.putLong(sessionId);
        buffer.putShort((short) tokenSize);
        buffer.put(sessionToken.getBytes());
        buffer.flip();
        byte[] bytes = EzyByteBuffers.getBytes(buffer);
        EzyUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
            DatagramChannel.open(),
            new InetSocketAddress(12345),
            bytes);
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySessionManager sessionManager = mock(EzySessionManager.class);
        EzySession session = spy(EzyAbstractSession.class);
        session.setToken(sessionToken);
        when(sessionManager.getSession(sessionId)).thenReturn(session);
        EzyResponseApi responseApi = mock(EzyResponseApi.class);
        EzyDatagramChannelPool channelPool = new EzyDatagramChannelPool(1);
        handler.setHandlerGroupManager(handlerGroupManager);
        handler.setSessionManager(sessionManager);
        handler.setResponseApi(responseApi);
        handler.setDatagramChannelPool(channelPool);
        handler.fireUdpPacketReceived(packet);
        Thread.sleep(200);
        handler.destroy();
    }

    @Test
    public void handleUdpHandshakeContentLengthLessThan11() {
        // given
        EzySimpleNioUdpDataHandler sut = new EzySimpleNioUdpDataHandler(1);

        DatagramChannel channel = mock(DatagramChannel.class);
        InetSocketAddress address = new InetSocketAddress(3005);
        EzyMessage message = mock(EzyMessage.class);
        when(message.getContent()).thenReturn(new byte[0]);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleUdpHandshake")
            .param(DatagramChannel.class, channel)
            .param(InetSocketAddress.class, address)
            .param(EzyMessage.class, message)
            .call();

        // then
        verify(message, times(1)).getContent();
        sut.destroy();
    }

    @Test
    public void handleUdpHandshakeTokenSizeGreaterThan512() {
        // given
        EzySimpleNioUdpDataHandler sut = new EzySimpleNioUdpDataHandler(1);

        DatagramChannel channel = mock(DatagramChannel.class);
        InetSocketAddress address = new InetSocketAddress(3005);
        EzyMessage message = mock(EzyMessage.class);

        String sessionToken = RandomUtil.randomAlphabetString(514);
        long sessionId = 12345L;
        int tokenSize = sessionToken.length();
        int messageSize = 0;
        messageSize += 8; // sessionIdSize
        messageSize += 2; // tokenLengthSize
        messageSize += tokenSize; // messageSize
        ByteBuffer buffer = ByteBuffer.allocate(messageSize);
        buffer.putLong(sessionId);
        buffer.putShort((short) tokenSize);
        buffer.put(sessionToken.getBytes());
        buffer.flip();
        byte[] bytes = EzyByteBuffers.getBytes(buffer);
        when(message.getContent()).thenReturn(bytes);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleUdpHandshake")
            .param(DatagramChannel.class, channel)
            .param(InetSocketAddress.class, address)
            .param(EzyMessage.class, message)
            .call();

        // then
        verify(message, times(1)).getContent();
        sut.destroy();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void handleUdpHandshakeSessionIsNull() {
        // given
        EzySimpleNioUdpDataHandler sut = new EzySimpleNioUdpDataHandler(1);

        DatagramChannel channel = mock(DatagramChannel.class);
        InetSocketAddress address = new InetSocketAddress(3005);
        EzyMessage message = mock(EzyMessage.class);

        String sessionToken = RandomUtil.randomAlphabetString(8);
        long sessionId = 12345L;
        int tokenSize = sessionToken.length();
        int messageSize = 0;
        messageSize += 8; // sessionIdSize
        messageSize += 2; // tokenLengthSize
        messageSize += tokenSize; // messageSize
        ByteBuffer buffer = ByteBuffer.allocate(messageSize);
        buffer.putLong(sessionId);
        buffer.putShort((short) tokenSize);
        buffer.put(sessionToken.getBytes());
        buffer.flip();
        byte[] bytes = EzyByteBuffers.getBytes(buffer);
        when(message.getContent()).thenReturn(bytes);

        EzySessionManager sessionManager = mock(EzySessionManager.class);
        sut.setSessionManager(sessionManager);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleUdpHandshake")
            .param(DatagramChannel.class, channel)
            .param(InetSocketAddress.class, address)
            .param(EzyMessage.class, message)
            .call();

        // then
        verify(message, times(1)).getContent();
        verify(sessionManager, times(1)).getSession(sessionId);
        sut.destroy();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void handleUdpHandshakeSessionTokenIsNotMatch() throws Exception {
        // given
        EzySimpleNioUdpDataHandler sut = new EzySimpleNioUdpDataHandler(1);

        DatagramChannel channel = mock(DatagramChannel.class);
        InetSocketAddress address = new InetSocketAddress(3005);
        EzyMessage message = mock(EzyMessage.class);

        String sessionToken = RandomUtil.randomAlphabetString(8);
        long sessionId = 12345L;
        int tokenSize = sessionToken.length();
        int messageSize = 0;
        messageSize += 8; // sessionIdSize
        messageSize += 2; // tokenLengthSize
        messageSize += tokenSize; // messageSize
        ByteBuffer buffer = ByteBuffer.allocate(messageSize);
        buffer.putLong(sessionId);
        buffer.putShort((short) tokenSize);
        buffer.put(sessionToken.getBytes());
        buffer.flip();
        byte[] bytes = EzyByteBuffers.getBytes(buffer);
        when(message.getContent()).thenReturn(bytes);

        EzySessionManager sessionManager = mock(EzySessionManager.class);
        sut.setSessionManager(sessionManager);

        EzySession session = mock(EzySession.class);
        when(session.getToken()).thenReturn(RandomUtil.randomShortAlphabetString());
        when(sessionManager.getSession(sessionId)).thenReturn(session);

        EzyResponseApi responseApi = mock(EzyResponseApi.class);
        sut.setResponseApi(responseApi);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleUdpHandshake")
            .param(DatagramChannel.class, channel)
            .param(InetSocketAddress.class, address)
            .param(EzyMessage.class, message)
            .call();

        // then
        verify(message, times(1)).getContent();
        verify(sessionManager, times(1)).getSession(sessionId);
        verify(session, times(1)).getToken();
        verify(responseApi, times(1)).response(any());
        sut.destroy();
    }

    @Test
    public void testNotHandshakeCase() throws Exception {
        String sessionToken = "12345678";
        long sessionId = 12345L;
        EzySimpleNioUdpDataHandler handler = new EzySimpleNioUdpDataHandler(1);
        int tokenSize = sessionToken.length();
        int messageSize = 0;
        messageSize += 8; // sessionIdSize
        messageSize += 2; // tokenLengthSize
        messageSize += tokenSize; // messageSize
        ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + messageSize);
        byte header = 0;
        buffer.put(header);
        buffer.putShort((short) messageSize);
        buffer.putLong(sessionId);
        buffer.putShort((short) tokenSize);
        buffer.put(sessionToken.getBytes());
        buffer.flip();
        byte[] bytes = EzyByteBuffers.getBytes(buffer);
        EzyUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
            DatagramChannel.open(),
            new InetSocketAddress(12345),
            bytes);
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzyNioHandlerGroup handlerGroup = mock(EzyNioHandlerGroup.class);
        when(handlerGroupManager.getHandlerGroup(packet.getAddress())).thenReturn(handlerGroup);
        EzyNioSession session = new EzySimpleSession();
        session.setToken(sessionToken);
        EzyChannel channel = mock(EzyChannel.class);
        when(channel.getClientAddress()).thenReturn(new InetSocketAddress(12345));
        session.setChannel(channel);
        when(handlerGroup.getSession()).thenReturn(session);
        handler.setHandlerGroupManager(handlerGroupManager);
        handler.fireUdpPacketReceived(packet);
        Thread.sleep(200);
        handler.destroy();
    }

    @Test
    public void testExceptionCase() throws Exception {
        String sessionToken = "12345678";
        long sessionId = 12345L;
        EzySimpleNioUdpDataHandler handler = new EzySimpleNioUdpDataHandler(1);
        int tokenSize = sessionToken.length();
        int messageSize = 0;
        messageSize += 8; // sessionIdSize
        messageSize += 2; // tokenLengthSize
        messageSize += tokenSize; // messageSize
        ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + messageSize);
        byte header = 0;
        buffer.put(header);
        buffer.putShort((short) messageSize);
        buffer.putLong(sessionId);
        buffer.putShort((short) tokenSize);
        buffer.put(sessionToken.getBytes());
        buffer.flip();
        byte[] bytes = EzyByteBuffers.getBytes(buffer);
        EzyUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
            DatagramChannel.open(),
            new InetSocketAddress(12345),
            bytes);
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        when(handlerGroupManager.getHandlerGroup(packet.getAddress())).thenThrow(new IllegalArgumentException("test"));
        handler.setHandlerGroupManager(handlerGroupManager);
        handler.fireUdpPacketReceived(packet);
        Thread.sleep(200);
        handler.destroy();
    }

    @Test
    public void testInvalidMessageCase() throws Exception {
        EzySimpleNioUdpDataHandler handler = new EzySimpleNioUdpDataHandler(1);
        byte[] bytes = new byte[]{1, 2, 3};
        EzyUdpReceivedPacket packet = new EzySimpleUdpReceivedPacket(
            DatagramChannel.open(),
            new InetSocketAddress(12345),
            bytes);
        handler.fireUdpPacketReceived(packet);
        Thread.sleep(200);
        handler.destroy();
    }

    @Test
    public void handleReceivedUdpPacketTest() throws Exception {
        // given
        EzySimpleNioUdpDataHandler sut = new EzySimpleNioUdpDataHandler(1);

        byte[] messageBytes = messageBytes();

        InetSocketAddress udpAddress = new InetSocketAddress(3005);
        InetSocketAddress tcpAddress = new InetSocketAddress(123);
        EzyUdpReceivedPacket packet = mock(EzyUdpReceivedPacket.class);
        when(packet.getBytes()).thenReturn(messageBytes);
        when(packet.getAddress()).thenReturn(udpAddress);

        SocketChannel socketChannel = mock(SocketChannel.class);
        when(socketChannel.getRemoteAddress()).thenReturn(tcpAddress);

        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        when(handlerGroupManager.getSocketChannel(udpAddress)).thenReturn(socketChannel);
        sut.setHandlerGroupManager(handlerGroupManager);

        EzySocketDataReceiver socketDataReceiver = mock(EzySocketDataReceiver.class);
        sut.setSocketDataReceiver(socketDataReceiver);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedUdpPacket")
            .param(EzyUdpReceivedPacket.class, packet)
            .call();

        // then
        verify(handlerGroupManager, times(1)).getSocketChannel(udpAddress);
        verify(socketChannel, times(1)).getRemoteAddress();
        verify(socketDataReceiver, times(1)).udpReceive(any(SocketChannel.class), any(EzyMessage.class));
        sut.destroy();
    }

    @Test
    public void handleReceivedUdpPacketWithSessionTest() {
        // given
        EzySimpleNioUdpDataHandler sut = new EzySimpleNioUdpDataHandler(1);

        byte[] messageBytes = messageBytes();

        InetSocketAddress udpAddress = new InetSocketAddress(3005);
        InetSocketAddress tcpAddress = new InetSocketAddress(123);
        EzyUdpReceivedPacket packet = mock(EzyUdpReceivedPacket.class);
        when(packet.getBytes()).thenReturn(messageBytes);
        when(packet.getAddress()).thenReturn(udpAddress);

        Session session = mock(Session.class);
        when(session.getRemoteAddress()).thenReturn(tcpAddress);

        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        when(handlerGroupManager.getSocketChannel(udpAddress)).thenReturn(session);
        sut.setHandlerGroupManager(handlerGroupManager);

        EzySocketDataReceiver socketDataReceiver = mock(EzySocketDataReceiver.class);
        sut.setSocketDataReceiver(socketDataReceiver);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedUdpPacket")
            .param(EzyUdpReceivedPacket.class, packet)
            .call();

        // then
        verify(handlerGroupManager, times(1)).getSocketChannel(udpAddress);
        verify(session, times(1)).getRemoteAddress();
        verify(socketDataReceiver, times(1)).udpReceive(any(SocketChannel.class), any(EzyMessage.class));
        sut.destroy();
    }

    @Test
    public void handleReceivedUdpPacketIsNotOneClientTest() throws Exception {
        // given
        EzySimpleNioUdpDataHandler sut = new EzySimpleNioUdpDataHandler(1);

        byte[] messageBytes = messageBytes();

        InetSocketAddress udpAddress = new InetSocketAddress("client1", 3005);
        InetSocketAddress tcpAddress = new InetSocketAddress("client2", 123);
        EzyUdpReceivedPacket packet = mock(EzyUdpReceivedPacket.class);
        when(packet.getBytes()).thenReturn(messageBytes);
        when(packet.getAddress()).thenReturn(udpAddress);

        SocketChannel socketChannel = mock(SocketChannel.class);
        when(socketChannel.getRemoteAddress()).thenReturn(tcpAddress);

        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        when(handlerGroupManager.getSocketChannel(udpAddress)).thenReturn(socketChannel);
        sut.setHandlerGroupManager(handlerGroupManager);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedUdpPacket")
            .param(EzyUdpReceivedPacket.class, packet)
            .call();

        // then
        verify(handlerGroupManager, times(1)).getSocketChannel(udpAddress);
        verify(socketChannel, times(1)).getRemoteAddress();
        sut.destroy();
    }

    @Test
    public void handleReceivedUdpPacketInvalidMessageTest() {
        // given
        EzySimpleNioUdpDataHandler sut = new EzySimpleNioUdpDataHandler(1);

        byte[] messageBytes = new byte[0];

        EzyUdpReceivedPacket packet = mock(EzyUdpReceivedPacket.class);
        when(packet.getBytes()).thenReturn(messageBytes);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("handleReceivedUdpPacket")
            .param(EzyUdpReceivedPacket.class, packet)
            .call();

        // then
        sut.destroy();
    }

    private byte[] messageBytes() {
        String sessionToken = "12345678";
        long sessionId = 12345L;
        int tokenSize = sessionToken.length();
        int messageSize = 0;
        messageSize += 8; // sessionIdSize
        messageSize += 2; // tokenLengthSize
        messageSize += tokenSize; // messageSize
        ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + messageSize);
        byte header = 0;
        buffer.put(header);
        buffer.putShort((short) messageSize);
        buffer.putLong(sessionId);
        buffer.putShort((short) tokenSize);
        buffer.put(sessionToken.getBytes());
        buffer.flip();
        return EzyByteBuffers.getBytes(buffer);
    }
}
