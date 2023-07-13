package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeHandler;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

public class EzySslHandshakeHandlerTest {

    private SSLContext sslContext;
    private EzySSLContextSpiForTest sslContextSpi;
    private SSLEngine sslEngine;
    private SSLSession sslSession;
    private SocketChannel socketChannel;
    private final int timeout = 50;
    private final int appBufferSize = 128;
    private final int packetBufferSize = 128;
    private EzySslHandshakeHandler instance;

    @BeforeMethod
    public void setup() {
        this.sslContext = mock(SSLContext.class);
        this.sslContextSpi = mock(EzySSLContextSpiForTest.class);
        this.sslEngine = mock(SSLEngine.class);
        this.sslSession = mock(SSLSession.class);
        this.socketChannel = mock(SocketChannel.class);
        this.instance = new EzySslHandshakeHandler(
            sslContext,
            timeout
        );
        FieldUtil.setFieldValue(
            sslContext,
            "contextSpi",
            sslContextSpi
        );
        when(sslContextSpi.engineCreateSSLEngine()).thenReturn(sslEngine);
        when(sslEngine.getSession()).thenReturn(sslSession);
        when(sslSession.getApplicationBufferSize()).thenReturn(appBufferSize);
        when(sslSession.getPacketBufferSize()).thenReturn(packetBufferSize);
    }

    @AfterMethod
    public void verifyAll() throws Exception {
        verifyNoMoreInteractions(sslContext);

        verify(sslContextSpi, times(1)).engineCreateSSLEngine();
        verifyNoMoreInteractions(sslContextSpi);

        verify(sslEngine, times(1)).beginHandshake();
        verify(sslEngine, times(1)).setUseClientMode(false);
        verify(sslEngine, times(1)).getSession();
        verifyNoMoreInteractions(sslEngine);

        verify(sslSession, times(1)).getPacketBufferSize();
        verify(sslSession, times(1)).getApplicationBufferSize();
        verifyNoMoreInteractions(sslSession);
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapEngineStatusIsOk() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NEED_UNWRAP
        );
        int readBytes = RandomUtil.randomSmallInt();
        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(readBytes);

        SSLEngineResult engineResult = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        when(
            sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenReturn(engineResult);

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapReadBytesLt0() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        });
        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(-1);

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(2)).getHandshakeStatus();
        verify(sslEngine, times(1)).isInboundDone();
        verify(sslEngine, times(1)).closeInbound();
        verify(sslEngine, times(1)).closeOutbound();
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapReadBytesLt0CloseInboundError() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        });

        SSLException error = new SSLException("test");
        doThrow(error).when(sslEngine).closeInbound();

        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(-1);

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(2)).getHandshakeStatus();
        verify(sslEngine, times(1)).isInboundDone();
        verify(sslEngine, times(1)).closeInbound();
        verify(sslEngine, times(1)).closeOutbound();
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapReadBytesLt0OutboundNotDone() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        });

        when(sslEngine.isInboundDone()).thenReturn(true);

        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(-1);

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(2)).getHandshakeStatus();
        verify(sslEngine, times(1)).isInboundDone();
        verify(sslEngine, times(1)).isOutboundDone();
        verify(sslEngine, times(1)).closeInbound();
        verify(sslEngine, times(1)).closeOutbound();
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapReadBytesLt0InOutboundDone() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        });

        when(sslEngine.isInboundDone()).thenReturn(true);
        when(sslEngine.isOutboundDone()).thenReturn(true);

        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(-1);

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.handle(socketChannel)
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1)).isInboundDone();
        verify(sslEngine, times(1)).isOutboundDone();
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapEngineUnwrapThrowException() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            return SSLEngineResult.HandshakeStatus.FINISHED;
        });
        int readBytes = RandomUtil.randomSmallInt();
        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(readBytes);

        SSLException error = new SSLException("test");
        when(
            sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenThrow(error);

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(2)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
        verify(sslEngine, times(1)).closeOutbound();
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapEngineStatusIsBufferOverflow() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            return SSLEngineResult.HandshakeStatus.FINISHED;
        });
        int readBytes = RandomUtil.randomSmallInt();
        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(readBytes);

        SSLEngineResult engineResultOverflow = new SSLEngineResult(
            SSLEngineResult.Status.BUFFER_OVERFLOW,
            SSLEngineResult.HandshakeStatus.NEED_UNWRAP,
            0,
            0
        );
        SSLEngineResult engineResultOk = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        AtomicInteger unwrapCallCount = new AtomicInteger();
        when(
            sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            int callCount = unwrapCallCount.incrementAndGet();
            if (callCount == 1) {
                return engineResultOverflow;
            }
            return engineResultOk;
        });

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(socketChannel, times(2))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(2))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapEngineStatusIsBufferUnderFlow() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            return SSLEngineResult.HandshakeStatus.FINISHED;
        });
        int readBytes = RandomUtil.randomSmallInt();
        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(readBytes);

        SSLEngineResult engineResultOverflow = new SSLEngineResult(
            SSLEngineResult.Status.BUFFER_UNDERFLOW,
            SSLEngineResult.HandshakeStatus.NEED_UNWRAP,
            0,
            0
        );
        SSLEngineResult engineResultOk = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        AtomicInteger unwrapCallCount = new AtomicInteger();
        when(
            sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            int callCount = unwrapCallCount.incrementAndGet();
            if (callCount == 1) {
                return engineResultOverflow;
            }
            return engineResultOk;
        });

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(socketChannel, times(2))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(2))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapEngineStatusIsClosed() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            return SSLEngineResult.HandshakeStatus.FINISHED;
        });
        int readBytes = RandomUtil.randomSmallInt();
        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(readBytes);

        SSLEngineResult engineResultOverflow = new SSLEngineResult(
            SSLEngineResult.Status.CLOSED,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        SSLEngineResult engineResultOk = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        AtomicInteger unwrapCallCount = new AtomicInteger();
        when(
            sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            int callCount = unwrapCallCount.incrementAndGet();
            if (callCount == 1) {
                return engineResultOverflow;
            }
            return engineResultOk;
        });

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(2)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
        verify(sslEngine, times(1)).isOutboundDone();
        verify(sslEngine, times(1)).closeOutbound();
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapEngineStatusIsClosedOutboundDone() throws Exception {
        // given
        when(sslEngine.isOutboundDone()).thenReturn(true);

        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            return SSLEngineResult.HandshakeStatus.FINISHED;
        });
        int readBytes = RandomUtil.randomSmallInt();
        when(socketChannel.read(any(ByteBuffer.class))).thenReturn(readBytes);

        SSLEngineResult engineResultOverflow = new SSLEngineResult(
            SSLEngineResult.Status.CLOSED,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        SSLEngineResult engineResultOk = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        AtomicInteger unwrapCallCount = new AtomicInteger();
        when(
            sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            int callCount = unwrapCallCount.incrementAndGet();
            if (callCount == 1) {
                return engineResultOverflow;
            }
            return engineResultOk;
        });

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.handle(socketChannel)
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
        verify(sslEngine, times(1)).isOutboundDone();
    }
}
