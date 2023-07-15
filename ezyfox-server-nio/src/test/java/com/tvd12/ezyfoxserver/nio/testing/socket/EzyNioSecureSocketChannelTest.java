package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfox.io.EzyByteBuffers;
import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketChannel;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EzyNioSecureSocketChannelTest {

    private byte[] bytes;
    private SSLEngine sslEngine;
    private SSLSession sslSession;
    private SocketChannel socketChannel;
    private EzyNioSecureSocketChannel instance;

    @BeforeMethod
    public void setup() {
        int bufferSize = 128;
        bytes = new byte[] {1, 2, 3};
        sslEngine = mock(SSLEngine.class);
        sslSession = mock(SSLSession.class);
        socketChannel = mock(SocketChannel.class);
        instance = new EzyNioSecureSocketChannel(
            socketChannel,
            sslEngine
        );
        when(sslEngine.getSession()).thenReturn(sslSession);
        when(sslSession.getPacketBufferSize()).thenReturn(bufferSize);
    }

    @AfterMethod
    public void verifyAll() throws Exception {
        Asserts.assertEquals(instance.getEngine(), sslEngine);

        verify(sslEngine, times(1)).getSession();
        verifyNoMoreInteractions(sslEngine);

        verify(sslSession, times(1)).getPacketBufferSize();
        verifyNoMoreInteractions(sslSession);

        verify(socketChannel, times(1)).getLocalAddress();
        verify(socketChannel, times(1)).getRemoteAddress();
        verifyNoMoreInteractions(socketChannel);
    }

    @Test
    public void packCaseOk() throws Exception {
        // given
        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        when(sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenAnswer(it -> {
                ByteBuffer inputBuffer = it.getArgumentAt(0, ByteBuffer.class);
                ByteBuffer netBuffer = it.getArgumentAt(1, ByteBuffer.class);
                netBuffer.put(EzyByteBuffers.getBytes(inputBuffer));
                return result;
            });

        // when
        byte[] actual = instance.pack(bytes);

        // then
        Asserts.assertEquals(actual, bytes);

        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readTcpBytesFromBufferCaseBufferOverFlow() throws Exception {
        // given
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

        AtomicInteger wrapCallCount = new AtomicInteger();
        when(sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenAnswer(it -> {
                int callCount = wrapCallCount.incrementAndGet();
                if (callCount == 1) {
                    return resultBufferOverflow;
                }
                ByteBuffer inputBuffer = it.getArgumentAt(0, ByteBuffer.class);
                ByteBuffer netBuffer = it.getArgumentAt(1, ByteBuffer.class);
                netBuffer.put(EzyByteBuffers.getBytes(inputBuffer));
                return resultOk;
            });

        // when
        byte[] actual = instance.pack(bytes);

        // then
        Asserts.assertEquals(actual, bytes);

        verify(sslEngine, times(2))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readTcpBytesFromBufferCaseBufferUnderFlow() throws Exception {
        // given
        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.BUFFER_UNDERFLOW,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        when(sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenReturn(result);

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.pack(bytes)
        );

        // then
        Asserts.assertEqualsType(e, IOException.class);

        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
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

        when(sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenReturn(result);

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.pack(bytes)
        );

        // then
        Asserts.assertEqualsType(e, EzyConnectionCloseException.class);

        verify(sslEngine, times(1)).closeOutbound();
        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readTcpBytesFromBufferNoRemaining() throws Exception {
        // when
        byte[] actual = instance.pack(new byte[0]);

        // then
        Asserts.assertEquals(actual, new byte[0]);
    }
}
