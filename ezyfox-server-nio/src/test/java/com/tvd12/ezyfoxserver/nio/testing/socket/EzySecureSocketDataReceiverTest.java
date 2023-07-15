package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketChannel;
import com.tvd12.ezyfoxserver.nio.socket.EzySecureSocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

public class EzySecureSocketDataReceiverTest {

    private ByteBuffer buffer;
    private SSLEngine sslEngine;
    private SSLSession sslSession;
    private EzyNioSecureSocketChannel channel;
    private EzyHandlerGroupManager handlerGroupManager;
    private EzySecureSocketDataReceiver instance;

    @BeforeMethod
    public void setup() {
        int bufferSize = 128;
        buffer = ByteBuffer.allocate(bufferSize);
        sslEngine = mock(SSLEngine.class);
        sslSession = mock(SSLSession.class);
        channel = mock(EzyNioSecureSocketChannel.class);
        handlerGroupManager = mock(EzyHandlerGroupManager.class);
        instance = (EzySecureSocketDataReceiver) EzySecureSocketDataReceiver
            .builder()
            .threadPoolSize(1)
            .handlerGroupManager(handlerGroupManager)
            .build();
        when(channel.getEngine()).thenReturn(sslEngine);
        when(sslEngine.getSession()).thenReturn(sslSession);
        when(sslSession.getApplicationBufferSize()).thenReturn(bufferSize);
        when(sslSession.getPacketBufferSize()).thenReturn(bufferSize);
    }

    @AfterMethod
    public void verifyAll() {
        verify(sslEngine, times(1)).getSession();
        verifyNoMoreInteractions(sslEngine);

        verify(sslSession, times(1)).getApplicationBufferSize();
        verify(sslSession, times(1)).getPacketBufferSize();
        verifyNoMoreInteractions(sslSession);

        verify(channel, times(1)).getEngine();
        verifyNoMoreInteractions(channel);
        verifyNoMoreInteractions(handlerGroupManager);
    }

    @Test
    public void readTcpBytesFromBufferCaseOk() throws Exception {
        // given
        buffer.clear();
        buffer.put(new byte[] {1, 2});
        buffer.flip();
        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenAnswer(it -> {
                ByteBuffer tcpNetBuffer = it.getArgumentAt(1, ByteBuffer.class);
                tcpNetBuffer.clear();
                tcpNetBuffer.put(buffer);
                return result;
            });

        // when
        byte[] actual = MethodInvoker.create()
            .object(instance)
            .method("readTcpBytesFromBuffer")
            .param(EzyChannel.class, channel)
            .param(ByteBuffer.class, buffer)
            .invoke(byte[].class);

        // then
        Asserts.assertEquals(actual, new byte[] {1, 2});

        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readTcpBytesFromBufferCaseBufferOverFlow() throws Exception {
        // given
        buffer.clear();
        buffer.put(new byte[] {1, 2});
        buffer.flip();
        SSLEngineResult resultBufferOverflow = new SSLEngineResult(
            SSLEngineResult.Status.BUFFER_OVERFLOW,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );
        SSLEngineResult resultOk = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        AtomicInteger unwrapCallCount = new AtomicInteger();
        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenAnswer(it -> {
                int callCount = unwrapCallCount.incrementAndGet();
                if (callCount == 1) {
                    return resultBufferOverflow;
                }
                ByteBuffer tcpNetBuffer = it.getArgumentAt(1, ByteBuffer.class);
                tcpNetBuffer.clear();
                tcpNetBuffer.put(buffer);
                return resultOk;
            });

        // when
        byte[] actual = MethodInvoker.create()
            .object(instance)
            .method("readTcpBytesFromBuffer")
            .param(EzyChannel.class, channel)
            .param(ByteBuffer.class, buffer)
            .invoke(byte[].class);

        // then
        Asserts.assertEquals(actual, new byte[] {1, 2});

        verify(sslEngine, times(2))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readTcpBytesFromBufferCaseBufferUnderFlow() throws Exception {
        // given
        buffer.clear();
        buffer.put(new byte[] {1, 2});
        buffer.flip();
        SSLEngineResult resultBufferOverflow = new SSLEngineResult(
            SSLEngineResult.Status.BUFFER_UNDERFLOW,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );
        SSLEngineResult resultOk = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        AtomicInteger unwrapCallCount = new AtomicInteger();
        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenAnswer(it -> {
                int callCount = unwrapCallCount.incrementAndGet();
                if (callCount == 1) {
                    return resultBufferOverflow;
                }
                ByteBuffer tcpNetBuffer = it.getArgumentAt(1, ByteBuffer.class);
                tcpNetBuffer.clear();
                tcpNetBuffer.put(buffer);
                return resultOk;
            });

        // when
        byte[] actual = MethodInvoker.create()
            .object(instance)
            .method("readTcpBytesFromBuffer")
            .param(EzyChannel.class, channel)
            .param(ByteBuffer.class, buffer)
            .invoke(byte[].class);

        // then
        Asserts.assertEquals(actual, new byte[] {1, 2});

        verify(sslEngine, times(2))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readTcpBytesFromBufferCaseBufferClosed() throws Exception {
        // given
        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.CLOSED,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenReturn(result);

        // when
        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(instance)
                .method("readTcpBytesFromBuffer")
                .param(EzyChannel.class, channel)
                .param(ByteBuffer.class, buffer)
                .invoke(byte[].class)
        );

        // then
        Asserts.assertEqualsType(e.getCause().getCause(), EzyConnectionCloseException.class);

        verify(sslEngine, times(1)).closeOutbound();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readTcpBytesFromBufferNoRemaining() {
        // given
        buffer.clear();
        buffer.flip();

        // when
        byte[] actual = MethodInvoker.create()
            .object(instance)
            .method("readTcpBytesFromBuffer")
            .param(EzyChannel.class, channel)
            .param(ByteBuffer.class, buffer)
            .invoke(byte[].class);

        // then
        Asserts.assertEquals(actual, new byte[0]);
    }

    @Test
    public void tcpReceiveCaseBufferClosed() throws Exception {
        // given
        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.CLOSED,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenReturn(result);

        SocketChannel socketChannel = mock(SocketChannel.class);
        byte[] bytes = new byte[] {1, 2, 3};
        when(socketChannel.read(any(ByteBuffer.class))).thenAnswer(it -> {
            ByteBuffer bf = it.getArgumentAt(0, ByteBuffer.class);
            bf.clear();
            bf.put(bytes);
            return bytes.length;
        });

        EzyNioHandlerGroup handlerGroup = mock(EzyNioHandlerGroup.class);
        when(handlerGroupManager.getHandlerGroup(socketChannel))
            .thenReturn(handlerGroup);
        when(handlerGroup.getChannel()).thenReturn(channel);

        // when
        instance.tcpReceive(socketChannel);
        Thread.sleep(300);

        // then
        verify(handlerGroupManager, times(2))
            .getHandlerGroup(socketChannel);

        verify(socketChannel, times(1)).read(any(ByteBuffer.class));
        verifyNoMoreInteractions(socketChannel);

        verify(sslEngine, times(1)).closeOutbound();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));

        verify(handlerGroup, times(1)).getChannel();
        verify(handlerGroup, times(1)).enqueueDisconnection();
        verifyNoMoreInteractions(handlerGroup);
    }
}
