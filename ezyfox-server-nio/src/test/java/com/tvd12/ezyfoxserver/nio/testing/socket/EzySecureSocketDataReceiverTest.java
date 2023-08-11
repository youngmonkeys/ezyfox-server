package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketChannel;
import com.tvd12.ezyfoxserver.nio.socket.EzySecureSocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.SSLException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.mockito.Mockito.*;

public class EzySecureSocketDataReceiverTest {

    private ByteBuffer buffer;
    private EzyNioSecureSocketChannel channel;
    private EzyHandlerGroupManager handlerGroupManager;
    private EzySecureSocketDataReceiver instance;

    @BeforeMethod
    public void setup() {
        int bufferSize = 128;
        buffer = ByteBuffer.allocate(bufferSize);
        channel = mock(EzyNioSecureSocketChannel.class);
        handlerGroupManager = mock(EzyHandlerGroupManager.class);
        instance = (EzySecureSocketDataReceiver) EzySecureSocketDataReceiver
            .builder()
            .threadPoolSize(1)
            .handlerGroupManager(handlerGroupManager)
            .build();
    }

    @AfterMethod
    public void verifyAll() {
        verifyNoMoreInteractions(channel);
        verifyNoMoreInteractions(handlerGroupManager);
    }

    @Test
    public void tcpReadBytesNormalCase() throws Exception {
        // given
        SocketChannel socketChannel = mock(SocketChannel.class);
        EzyNioHandlerGroup handlerGroup = mock(EzyNioHandlerGroup.class);
        when(handlerGroupManager.getHandlerGroup(socketChannel))
            .thenReturn(handlerGroup);
        when(handlerGroup.getChannel()).thenReturn(channel);

        // when
        MethodInvoker.create()
            .object(instance)
            .method("tcpReadBytes")
            .param(SocketChannel.class, socketChannel)
            .param(ByteBuffer.class, buffer)
            .invoke();

        // then
        verify(handlerGroupManager, times(1))
            .getHandlerGroup(socketChannel);

        verify(handlerGroup, times(1)).getChannel();
        verifyNoMoreInteractions(handlerGroup);

        verify(channel, times(1)).isHandshaked();
        verify(channel, times(1)).handshake();
    }

    @Test
    public void tcpReadBytesHandshakeFailedCase() throws Exception {
        // given
        SocketChannel socketChannel = mock(SocketChannel.class);
        EzyNioHandlerGroup handlerGroup = mock(EzyNioHandlerGroup.class);
        when(handlerGroupManager.getHandlerGroup(socketChannel))
            .thenReturn(handlerGroup);
        when(handlerGroup.getChannel()).thenReturn(channel);

        SSLException exception = new SSLException("test");
        doThrow(exception).when(channel).handshake();

        // when
        MethodInvoker.create()
            .object(instance)
            .method("tcpReadBytes")
            .param(SocketChannel.class, socketChannel)
            .param(ByteBuffer.class, buffer)
            .invoke();

        // then
        verify(handlerGroupManager, times(1))
            .getHandlerGroup(socketChannel);

        verify(handlerGroup, times(1)).getChannel();
        verify(handlerGroup, times(1)).enqueueDisconnection(
            EzyDisconnectReason.SSH_HANDSHAKE_FAILED
        );
        verifyNoMoreInteractions(handlerGroup);

        verify(channel, times(1)).isHandshaked();
        verify(channel, times(1)).handshake();
    }

    @Test
    public void tcpReadBytesAlreadyHandshakeCase() {
        // given
        SocketChannel socketChannel = mock(SocketChannel.class);
        EzyNioHandlerGroup handlerGroup = mock(EzyNioHandlerGroup.class);
        when(handlerGroupManager.getHandlerGroup(socketChannel))
            .thenReturn(handlerGroup);
        when(handlerGroup.getChannel()).thenReturn(channel);
        when(channel.isHandshaked()).thenReturn(true);

        // when
        MethodInvoker.create()
            .object(instance)
            .method("tcpReadBytes")
            .param(SocketChannel.class, socketChannel)
            .param(ByteBuffer.class, buffer)
            .invoke();

        // then
        verify(handlerGroupManager, times(1))
            .getHandlerGroup(socketChannel);

        verify(handlerGroup, times(1)).getChannel();
        verifyNoMoreInteractions(handlerGroup);

        verify(channel, times(1)).isHandshaked();
    }

    @Test
    public void tcpReadBytesHandleGroupNullCase() throws Exception {
        // given
        SocketChannel socketChannel = mock(SocketChannel.class);

        // when
        MethodInvoker.create()
            .object(instance)
            .method("tcpReadBytes")
            .param(SocketChannel.class, socketChannel)
            .param(ByteBuffer.class, buffer)
            .invoke();

        // then
        verify(handlerGroupManager, times(1))
            .getHandlerGroup(socketChannel);
    }

    @Test
    public void readTcpBytesFromBufferTest() throws Exception {
        // given
        byte[] bytes = RandomUtil.randomShortByteArray();
        when(channel.read(buffer)).thenReturn(bytes);

        // when
        byte[] actual = MethodInvoker.create()
            .object(instance)
            .method("readTcpBytesFromBuffer")
            .param(EzyChannel.class, channel)
            .param(ByteBuffer.class, buffer)
            .invoke(byte[].class);

        // then
        Asserts.assertEquals(actual, bytes);
        verify(channel, times(1)).read(buffer);
    }
}
