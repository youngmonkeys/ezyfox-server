package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.util.RandomUtil;
import org.eclipse.jetty.websocket.api.Session;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import static org.mockito.Mockito.*;

public class EzySocketDataReceiverTest {

    @Test
    public void tcpReadBytesClosedChannelException() throws Exception {
        // given
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySocketDataReceiver sut = EzySocketDataReceiver.builder()
            .handlerGroupManager(handlerGroupManager)
            .build();

        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{1, 2, 3});

        SocketChannel channel = mock(SocketChannel.class);
        when(channel.read(buffer)).thenThrow(new ClosedChannelException());

        // when
        MethodInvoker.create()
            .object(sut)
            .method("tcpReadBytes")
            .param(SocketChannel.class, channel)
            .param(ByteBuffer.class, buffer)
            .call();

        // then
        verify(handlerGroupManager, times(1)).getHandlerGroup(channel);
    }

    @Test
    public void processReadBytesHandlerGroupIsNull() throws Exception {
        // given
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySocketDataReceiver sut = EzySocketDataReceiver.builder()
            .handlerGroupManager(handlerGroupManager)
            .build();

        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{1, 2, 3});

        SocketChannel channel = mock(SocketChannel.class);
        when(channel.read(buffer)).thenThrow(new ClosedChannelException());

        // when
        MethodInvoker.create()
            .object(sut)
            .method("processTcpReadBytes")
            .param(SocketChannel.class, channel)
            .param(ByteBuffer.class, buffer)
            .call();

        // then
        verify(handlerGroupManager, times(1)).getHandlerGroup(channel);
    }

    @Test
    public void udpReceiveTest() throws Exception {
        // given
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySocketDataReceiver sut = EzySocketDataReceiver.builder()
            .handlerGroupManager(handlerGroupManager)
            .threadPoolSize(1)
            .build();

        SocketChannel channel = mock(SocketChannel.class);

        EzyMessage message = mock(EzyMessage.class);

        EzyNioHandlerGroup handlerGroup = mock(EzyNioHandlerGroup.class);
        when(handlerGroupManager.getHandlerGroup(channel)).thenReturn(handlerGroup);

        // when
        sut.udpReceive(channel, message);
        Thread.sleep(120);

        // then
        verify(handlerGroupManager, times(1)).getHandlerGroup(channel);
        verify(handlerGroup, times(1)).fireMessageReceived(message);
        sut.destroy();
    }

    @Test
    public void udpReceiveButHandlerGroupNullTest() throws Exception {
        // given
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySocketDataReceiver sut = EzySocketDataReceiver.builder()
            .handlerGroupManager(handlerGroupManager)
            .threadPoolSize(1)
            .build();

        SocketChannel channel = mock(SocketChannel.class);

        EzyMessage message = mock(EzyMessage.class);

        // when
        sut.udpReceive(channel, message);
        Thread.sleep(120);

        // then
        verify(handlerGroupManager, times(1)).getHandlerGroup(channel);
        sut.destroy();
    }

    @Test
    public void udpReceiveButExceptionTest() throws Exception {
        // given
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySocketDataReceiver sut = EzySocketDataReceiver.builder()
            .handlerGroupManager(handlerGroupManager)
            .threadPoolSize(1)
            .build();

        SocketChannel channel = mock(SocketChannel.class);

        EzyMessage message = mock(EzyMessage.class);

        EzyNioHandlerGroup handlerGroup = mock(EzyNioHandlerGroup.class);
        doThrow(new RuntimeException()).when(handlerGroup).fireMessageReceived(message);
        when(handlerGroupManager.getHandlerGroup(channel)).thenReturn(handlerGroup);

        // when
        sut.udpReceive(channel, message);
        Thread.sleep(120);

        // then
        verify(handlerGroupManager, times(1)).getHandlerGroup(channel);
        verify(handlerGroup, times(1)).fireMessageReceived(message);
        sut.destroy();
    }

    @Test
    public void doWsReceiveButException() throws Exception {
        // given
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySocketDataReceiver sut = EzySocketDataReceiver.builder()
            .handlerGroupManager(handlerGroupManager)
            .build();

        Session session = mock(Session.class);
        String message = RandomUtil.randomShortAlphabetString();

        EzyWsHandlerGroup handlerGroup = mock(EzyWsHandlerGroup.class);
        doThrow(new RuntimeException()).when(handlerGroup).fireBytesReceived(message);
        when(handlerGroupManager.getHandlerGroup(session)).thenReturn(handlerGroup);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("doWsReceive")
            .param(Session.class, session)
            .param(String.class, message)
            .call();

        // then
        verify(handlerGroupManager, times(1)).getHandlerGroup(session);
        verify(handlerGroup, times(1)).fireBytesReceived(message);
    }

    @Test
    public void doWsReceive2ButException() throws Exception {
        // given
        EzyHandlerGroupManager handlerGroupManager = mock(EzyHandlerGroupManager.class);
        EzySocketDataReceiver sut = EzySocketDataReceiver.builder()
            .handlerGroupManager(handlerGroupManager)
            .build();

        Session session = mock(Session.class);
        byte[] payload = new byte[]{1, 2, 3};

        EzyWsHandlerGroup handlerGroup = mock(EzyWsHandlerGroup.class);
        doThrow(new RuntimeException()).when(handlerGroup).fireBytesReceived(payload, 0, payload.length);
        when(handlerGroupManager.getHandlerGroup(session)).thenReturn(handlerGroup);

        // when
        MethodInvoker.create()
            .object(sut)
            .method("doWsReceive")
            .param(Session.class, session)
            .param(byte[].class, payload)
            .param(int.class, 0)
            .param(int.class, payload.length)
            .call();

        // then
        verify(handlerGroupManager, times(1)).getHandlerGroup(session);
        verify(handlerGroup, times(1)).fireBytesReceived(payload, 0, payload.length);
    }
}
