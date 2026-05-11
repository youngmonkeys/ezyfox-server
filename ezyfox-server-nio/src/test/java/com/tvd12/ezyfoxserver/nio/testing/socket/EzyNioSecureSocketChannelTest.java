package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfox.io.EzyByteBuffers;
import com.tvd12.ezyfox.util.EzyThreads;
import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketChannel;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EzyNioSecureSocketChannelTest {

    private byte[] bytes;
    private ByteBuffer buffer;
    private ByteBuffer netBuffer;
    private SSLEngine sslEngine;
    private SSLContext sslContext;
    private EzySSLContextSpiForTest sslContextSpi;
    private SSLSession sslSession;
    private SocketChannel socketChannel;
    private EzyNioSecureSocketChannel instance;
    private final int bufferSize = 128;
    private static final int SSL_HANDSHAKE_TIMEOUT = 100;
    private static final int MAX_REQUEST_SIZE = 128;

    @BeforeMethod
    public void setup() {
        bytes = new byte[] {1, 2, 3};
        buffer = ByteBuffer.allocate(bufferSize);
        netBuffer = ByteBuffer.allocate(bufferSize + 1);
        sslContext = mock(SSLContext.class);
        sslContextSpi = mock(EzySSLContextSpiForTest.class);
        sslEngine = mock(SSLEngine.class);
        sslSession = mock(SSLSession.class);
        socketChannel = mock(SocketChannel.class);
        EzySslContextProxy sslContextProxy = new EzySslContextProxy(() ->
            sslContext
        );
        instance = new EzyNioSecureSocketChannel(
            socketChannel,
            sslContextProxy,
            SSL_HANDSHAKE_TIMEOUT,
            MAX_REQUEST_SIZE
        );
        FieldUtil.setFieldValue(
            sslContext,
            "contextSpi",
            sslContextSpi
        );
        when(sslContextSpi.engineCreateSSLEngine()).thenReturn(sslEngine);
        when(sslEngine.getSession()).thenReturn(sslSession);
        when(sslSession.getPacketBufferSize()).thenReturn(bufferSize);
        when(sslSession.getApplicationBufferSize()).thenReturn(bufferSize);
        when(socketChannel.isConnected()).thenReturn(true);
        sslContextProxy.reloadSsl();
    }

    public void beforeNotHandshakeMethod() {
        FieldUtil.setFieldValue(instance, "engine", sslEngine);
        FieldUtil.setFieldValue(instance, "netBuffer", netBuffer);
        FieldUtil.setFieldValue(instance, "appBufferSize", bufferSize);
        FieldUtil.setFieldValue(instance, "netBufferSize", bufferSize);
        AtomicBoolean handshakeComplete = FieldUtil.getFieldValue(instance, "handshakeComplete");
        handshakeComplete.set(true);
    }

    @AfterMethod
    public void verifyAll() throws Exception {
        verifyNoMoreInteractions(sslContext);
        verifyNoMoreInteractions(sslContextSpi);
        verifyNoMoreInteractions(sslEngine);
        verifyNoMoreInteractions(sslSession);
        verify(socketChannel, times(1)).getLocalAddress();
        verify(socketChannel, times(1)).getRemoteAddress();
        verifyNoMoreInteractions(socketChannel);
    }

    private void verifyAfterHandshake() throws Exception {
        verify(sslContextSpi, times(1)).engineCreateSSLEngine();

        verify(sslEngine, times(1)).getSession();
        verify(sslEngine, times(1)).setUseClientMode(false);
        verify(sslEngine, times(1)).beginHandshake();

        verify(sslSession, times(1)).getApplicationBufferSize();
        verify(sslSession, times(1)).getPacketBufferSize();

        verify(socketChannel, times(1)).isConnected();
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
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());
        Asserts.assertNotNull(instance.getPackingLock());

        verifyAfterHandshake();
        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakePreserveRemainingPeerNetData() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NEED_UNWRAP
        );
        when(socketChannel.read(any(ByteBuffer.class))).thenAnswer(it -> {
            ByteBuffer buffer = it.getArgumentAt(0, ByteBuffer.class);
            buffer.put(new byte[] {1, 2, 3, 4});
            return 4;
        });

        SSLEngineResult engineResult = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.FINISHED,
            2,
            0
        );
        when(
            sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            ByteBuffer source = it.getArgumentAt(0, ByteBuffer.class);
            source.get();
            source.get();
            return engineResult;
        });

        // when
        instance.handshake();

        // then
        ByteBuffer actualNetBuffer = FieldUtil.getFieldValue(instance, "netBuffer");
        Asserts.assertEquals(actualNetBuffer.position(), 2);

        verifyAfterHandshake();
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
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
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
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
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
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
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
            instance.handshake()
        );

        // then
        verifyAfterHandshake();
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
        instance.handshake();

        // then
        verifyAfterHandshake();
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
        Throwable e = Asserts.assertThrows(() -> instance.handshake());

        // then
        Asserts.assertEqualsType(e, EzyConnectionCloseException.class);

        verifyAfterHandshake();
        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
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
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
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
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
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
            instance.handshake()
        );

        // then
        verifyAfterHandshake();

        Asserts.assertEqualsType(e, SSLException.class);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
        verify(sslEngine, times(1)).isOutboundDone();
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedWrapEngineStatusIsOk() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NEED_WRAP
        );
        when(socketChannel.write(any(ByteBuffer.class))).thenAnswer(it -> {
            ByteBuffer buffer = it.getArgumentAt(0, ByteBuffer.class);
            EzyByteBuffers.getBytes(buffer);
            return bufferSize;
        });

        SSLEngineResult engineResult = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        when(
            sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            ByteBuffer buffer = it.getArgumentAt(1, ByteBuffer.class);
            buffer.clear();
            buffer.put(new byte[] {1, 2, 3});
            return engineResult;
        });

        // when
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
        verify(socketChannel, times(1))
            .write(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedWrapEngineThrowException() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
            return SSLEngineResult.HandshakeStatus.FINISHED;
        });

        SSLException error = new SSLException("test");
        when(
            sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenThrow(error);

        // when
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();

        verify(sslEngine, times(2)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
        verify(sslEngine, times(1)).closeOutbound();
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedWrapEngineStatusIsBufferOverflow() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NEED_WRAP
        );
        when(socketChannel.write(any(ByteBuffer.class))).thenAnswer(it -> {
            ByteBuffer buffer = it.getArgumentAt(0, ByteBuffer.class);
            EzyByteBuffers.getBytes(buffer);
            return bufferSize;
        });

        SSLEngineResult engineResultBufferOverflow = new SSLEngineResult(
            SSLEngineResult.Status.BUFFER_OVERFLOW,
            SSLEngineResult.HandshakeStatus.NEED_WRAP,
            0,
            0
        );
        SSLEngineResult engineResultOk = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        AtomicInteger wrapCallCount = new AtomicInteger();
        when(
            sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            int callCount = wrapCallCount.incrementAndGet();
            if (callCount == 1) {
                return engineResultBufferOverflow;
            }
            ByteBuffer buffer = it.getArgumentAt(1, ByteBuffer.class);
            buffer.clear();
            buffer.put(new byte[] {1, 2, 3});
            return engineResultOk;
        });

        // when
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
        verify(socketChannel, times(1))
            .write(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(2))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedWrapEngineStatusIsBufferUnderflow() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NEED_WRAP
        );
        when(socketChannel.write(any(ByteBuffer.class))).thenAnswer(it -> {
            ByteBuffer buffer = it.getArgumentAt(0, ByteBuffer.class);
            EzyByteBuffers.getBytes(buffer);
            return bufferSize;
        });

        SSLEngineResult engineResult = new SSLEngineResult(
            SSLEngineResult.Status.BUFFER_UNDERFLOW,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );
        when(
            sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenReturn(engineResult);

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.handshake()
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);

        verifyAfterHandshake();

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedWrapEngineStatusIsClosed() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NEED_WRAP
        );
        when(socketChannel.write(any(ByteBuffer.class))).thenAnswer(it -> {
            ByteBuffer buffer = it.getArgumentAt(0, ByteBuffer.class);
            EzyByteBuffers.getBytes(buffer);
            return bufferSize;
        });

        SSLEngineResult engineResult = new SSLEngineResult(
            SSLEngineResult.Status.CLOSED,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        when(
            sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            ByteBuffer buffer = it.getArgumentAt(1, ByteBuffer.class);
            buffer.clear();
            buffer.put(new byte[] {1, 2, 3});
            return engineResult;
        });

        // when
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
        verify(socketChannel, times(1))
            .write(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedWrapEngineStatusIsClosedWriteThrowException() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
            return SSLEngineResult.HandshakeStatus.FINISHED;
        });
        IOException error = new IOException("test");
        when(socketChannel.write(any(ByteBuffer.class))).thenThrow(error);

        SSLEngineResult engineResult = new SSLEngineResult(
            SSLEngineResult.Status.CLOSED,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        when(
            sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            ByteBuffer buffer = it.getArgumentAt(1, ByteBuffer.class);
            buffer.clear();
            buffer.put(new byte[] {1, 2, 3});
            return engineResult;
        });

        // when
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
        verify(socketChannel, times(1))
            .write(any(ByteBuffer.class));

        verify(sslEngine, times(2)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedWrapEngineStatusIsClosedWriteTimeout() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NEED_WRAP
        );
        when(socketChannel.write(any(ByteBuffer.class))).thenAnswer(it -> {
            EzyThreads.sleep(SSL_HANDSHAKE_TIMEOUT * 2);
           return 0;
        });

        SSLEngineResult engineResult = new SSLEngineResult(
            SSLEngineResult.Status.CLOSED,
            SSLEngineResult.HandshakeStatus.NEED_WRAP,
            0,
            0
        );
        when(
            sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenAnswer(it -> {
            ByteBuffer buffer = it.getArgumentAt(1, ByteBuffer.class);
            buffer.clear();
            buffer.put(new byte[] {1, 2, 3});
            return engineResult;
        });

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.handshake()
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);

        verifyAfterHandshake();
        verify(socketChannel, times(1))
            .write(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedTask() throws Exception {
        // given
        AtomicInteger getHandshakeStatusCallCount = new AtomicInteger();
        when(sslEngine.getHandshakeStatus()).thenAnswer(it -> {
            int callCount = getHandshakeStatusCallCount.incrementAndGet();
            if (callCount == 1) {
                return SSLEngineResult.HandshakeStatus.NEED_TASK;
            }
            return SSLEngineResult.HandshakeStatus.FINISHED;
        });
        Runnable task = mock(Runnable.class);
        AtomicInteger getDelegatedTaskCallCount = new AtomicInteger();
        when(sslEngine.getDelegatedTask()).thenAnswer(it -> {
            int callCount = getDelegatedTaskCallCount.incrementAndGet();
            if (callCount == 1) {
                return task;
            }
            return null;
        });

        // when
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
        verify(sslEngine, times(2)).getHandshakeStatus();
        verify(sslEngine, times(2)).getDelegatedTask();

        verify(task, times(1)).run();
        verifyNoMoreInteractions(task);
    }

    @Test
    public void handleCaseHandshakeStatusIsFinish() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.FINISHED
        );

        // when
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
        verify(sslEngine, times(1)).getHandshakeStatus();
    }

    @Test
    public void handleCaseHandshakeStatusIsNotHandshake() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING
        );

        // when
        instance.handshake();

        // then
        Asserts.assertTrue(instance.isHandshakeComplete());

        verifyAfterHandshake();
        verify(sslEngine, times(1)).getHandshakeStatus();
    }

    @Test
    public void handleCaseHandshakeStatusIsNeedUnwrapTimeout() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NEED_UNWRAP
        );
        int readBytes = RandomUtil.randomSmallInt();
        when(socketChannel.read(any(ByteBuffer.class))).thenAnswer(it -> {
            EzyThreads.sleep(SSL_HANDSHAKE_TIMEOUT * 2);
            return readBytes;
        });

        SSLEngineResult engineResult = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NEED_WRAP,
            0,
            0
        );
        when(
            sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenReturn(engineResult);

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.handshake()
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);

        verifyAfterHandshake();
        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void handleCaseHandshakeChannelNotConnected() throws Exception {
        // given
        when(socketChannel.isConnected()).thenReturn(false);

        // whe
        instance.handshake();

        // then
        Asserts.assertFalse(instance.isHandshakeComplete());

        verify(socketChannel, times(1)).isConnected();
    }

    @Test
    public void handleCaseHandshakeEngineNotNull() throws Exception {
        // given
        FieldUtil.setFieldValue(instance, "engine", sslEngine);

        // whe
        instance.handshake();

        // then
        Asserts.assertFalse(instance.isHandshakeComplete());

        verify(socketChannel, times(1)).isConnected();
    }

    @Test
    public void packCaseOk() throws Exception {
        // given
        beforeNotHandshakeMethod();

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
    public void packCaseOutboundNetBufferAlreadyInitialized() throws Exception {
        // given
        beforeNotHandshakeMethod();
        FieldUtil.setFieldValue(
            instance,
            "outboundNetBuffer",
            ByteBuffer.allocate(bufferSize)
        );

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
    public void packCaseNoProgress() throws Exception {
        // given
        beforeNotHandshakeMethod();

        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        when(sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenReturn(result);

        // when
        byte[] actual = instance.pack(bytes);

        // then
        Asserts.assertEquals(actual, new byte[0]);

        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void packCaseConsumedOnlyProgress() throws Exception {
        // given
        beforeNotHandshakeMethod();

        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            bytes.length,
            0
        );

        when(sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenAnswer(it -> {
                ByteBuffer inputBuffer = it.getArgumentAt(0, ByteBuffer.class);
                inputBuffer.position(inputBuffer.limit());
                return result;
            });

        // when
        byte[] actual = instance.pack(bytes);

        // then
        Asserts.assertEquals(actual, new byte[0]);

        verify(sslEngine, times(1))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void packCaseProducedOnlyProgressThenConsumed() throws Exception {
        // given
        beforeNotHandshakeMethod();

        SSLEngineResult resultProducedOnly = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            bytes.length
        );
        SSLEngineResult resultConsumedOnly = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            bytes.length,
            0
        );

        AtomicInteger wrapCallCount = new AtomicInteger();
        when(sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenAnswer(it -> {
                int callCount = wrapCallCount.incrementAndGet();
                ByteBuffer inputBuffer = it.getArgumentAt(0, ByteBuffer.class);
                ByteBuffer netBuffer = it.getArgumentAt(1, ByteBuffer.class);
                if (callCount == 1) {
                    netBuffer.put(bytes);
                    return resultProducedOnly;
                }
                inputBuffer.position(inputBuffer.limit());
                return resultConsumedOnly;
            });

        // when
        byte[] actual = instance.pack(bytes);

        // then
        Asserts.assertEquals(actual, bytes);

        verify(sslEngine, times(2))
            .wrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void packCaseBufferOverFlow() throws Exception {
        // given
        beforeNotHandshakeMethod();

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
    public void packCaseBufferUnderFlow() throws Exception {
        // given
        beforeNotHandshakeMethod();

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
    public void packCaseBufferClosed() throws Exception {
        // given
        beforeNotHandshakeMethod();

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
    public void packCaseBufferClosedButException() throws Exception {
        // given
        beforeNotHandshakeMethod();

        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.CLOSED,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        when(sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenReturn(result);
        RuntimeException exception = new RuntimeException("test");
        doThrow(exception).when(sslEngine).closeOutbound();

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
    public void packNoRemaining() throws Exception {
        // when
        beforeNotHandshakeMethod();

        byte[] actual = instance.pack(new byte[0]);

        // then
        Asserts.assertEquals(actual, new byte[0]);
    }

    @Test
    public void packBeforeHandshake() {
        // when
        beforeNotHandshakeMethod();
        AtomicBoolean handshakeComplete = FieldUtil.getFieldValue(instance, "handshakeComplete");
        handshakeComplete.set(false);

        Throwable e = Asserts.assertThrows(() ->
            instance.pack(new byte[0])
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);
    }

    @Test
    public void readCaseOk() throws Exception {
        // given
        beforeNotHandshakeMethod();

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
                tcpNetBuffer.put(netBuffer);
                return result;
            });

        // when
        byte[] actual = instance.read(buffer);

        // then
        Asserts.assertEquals(actual, new byte[] {1, 2});

        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readBeforeHandshake() throws Exception {
        // given
        beforeNotHandshakeMethod();
        AtomicBoolean handshakeComplete =
            FieldUtil.getFieldValue(instance, "handshakeComplete");
        handshakeComplete.set(false);

        buffer.clear();
        buffer.flip();

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.read(buffer)
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);
    }

    @Test
    public void readCaseAppBufferAlreadyInitializedAndMaxNetBufferSizePositive() throws Exception {
        // given
        beforeNotHandshakeMethod();
        FieldUtil.setFieldValue(instance, "appBuffer", ByteBuffer.allocate(bufferSize));
        FieldUtil.setFieldValue(instance, "sslMaxNetBufferSize", bufferSize * 2);

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
                tcpNetBuffer.put(netBuffer);
                return result;
            });

        // when
        byte[] actual = instance.read(buffer);

        // then
        Asserts.assertEquals(actual, new byte[] {1, 2});

        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readCaseNoProgress() throws Exception {
        // given
        beforeNotHandshakeMethod();

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
            .thenReturn(result);

        // when
        byte[] actual = instance.read(buffer);

        // then
        Asserts.assertEquals(actual, new byte[0]);

        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readCaseRequiredNetBufferCapacityOverMax() throws Exception {
        // given
        beforeNotHandshakeMethod();
        netBuffer = ByteBuffer.allocate(1);
        FieldUtil.setFieldValue(instance, "netBuffer", netBuffer);
        FieldUtil.setFieldValue(instance, "sslMaxNetBufferSize", 1);

        buffer.clear();
        buffer.put(new byte[] {1, 2});
        buffer.flip();

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.read(buffer)
        );

        // then
        Asserts.assertEqualsType(e, EzyConnectionCloseException.class);
    }

    @Test
    public void readCaseGrowNetBufferCapacityClampedByMax() throws Exception {
        // given
        beforeNotHandshakeMethod();
        netBuffer = ByteBuffer.allocate(3);
        FieldUtil.setFieldValue(instance, "netBuffer", netBuffer);
        FieldUtil.setFieldValue(instance, "sslMaxNetBufferSize", 5);

        buffer.clear();
        buffer.put(new byte[] {1, 2, 3, 4, 5});
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
                tcpNetBuffer.put(new byte[] {1, 2});
                return result;
            });

        // when
        byte[] actual = instance.read(buffer);

        // then
        ByteBuffer actualNetBuffer = FieldUtil.getFieldValue(instance, "netBuffer");
        Asserts.assertEquals(actualNetBuffer.capacity(), 5);
        Asserts.assertEquals(actual, new byte[] {1, 2});

        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void ensureRemainingCaseGrowCapacityNormally() throws Exception {
        // given
        beforeNotHandshakeMethod();
        FieldUtil.setFieldValue(instance, "sslMaxNetBufferSize", 8);
        ByteBuffer currentBuffer = ByteBuffer.allocate(2);
        currentBuffer.put((byte) 1);
        currentBuffer.put((byte) 2);

        // when
        ByteBuffer actual = MethodInvoker.create()
            .object(instance)
            .method("ensureRemaining")
            .param(ByteBuffer.class, currentBuffer)
            .param(int.class, 1)
            .call();

        // then
        Asserts.assertEquals(actual.capacity(), 4);
        Asserts.assertEquals(actual.position(), 2);
    }

    @Test
    public void ensureRemainingCaseGrowCapacityToMax() throws Exception {
        // given
        beforeNotHandshakeMethod();
        FieldUtil.setFieldValue(instance, "sslMaxNetBufferSize", 7);
        ByteBuffer currentBuffer = ByteBuffer.allocate(4);
        currentBuffer.put(new byte[] {1, 2, 3, 4});

        // when
        ByteBuffer actual = MethodInvoker.create()
            .object(instance)
            .method("ensureRemaining")
            .param(ByteBuffer.class, currentBuffer)
            .param(int.class, 3)
            .call();

        // then
        Asserts.assertEquals(actual.capacity(), 7);
        Asserts.assertEquals(actual.position(), 4);
    }

    @Test
    public void readCaseFirstDrainOverMaxSize() throws Exception {
        // given
        beforeNotHandshakeMethod();
        FieldUtil.setFieldValue(instance, "appBuffer", ByteBuffer.allocate(8));
        FieldUtil.setFieldValue(instance, "sslMaxAppBufferSize", 5);

        buffer.clear();
        buffer.put(new byte[] {1, 2});
        buffer.flip();
        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            1,
            6
        );

        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenAnswer(it -> {
                ByteBuffer source = it.getArgumentAt(0, ByteBuffer.class);
                ByteBuffer tcpNetBuffer = it.getArgumentAt(1, ByteBuffer.class);
                source.get();
                tcpNetBuffer.put(new byte[] {1, 2, 3, 4, 5, 6});
                return result;
            });

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.read(buffer)
        );

        // then
        Asserts.assertEqualsType(e, EzyConnectionCloseException.class);

        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readCaseAccumulatedDrainOverMaxSize() throws Exception {
        // given
        beforeNotHandshakeMethod();
        FieldUtil.setFieldValue(instance, "appBuffer", ByteBuffer.allocate(4));
        FieldUtil.setFieldValue(instance, "sslMaxAppBufferSize", 5);

        buffer.clear();
        buffer.put(new byte[] {1, 2});
        buffer.flip();
        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            1,
            4
        );

        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenAnswer(it -> {
                ByteBuffer source = it.getArgumentAt(0, ByteBuffer.class);
                ByteBuffer tcpNetBuffer = it.getArgumentAt(1, ByteBuffer.class);
                source.get();
                tcpNetBuffer.put(new byte[] {1, 2, 3, 4});
                return result;
            });

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.read(buffer)
        );

        // then
        Asserts.assertEqualsType(e, EzyConnectionCloseException.class);

        verify(sslEngine, times(2))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void drainBufferCaseAppendToExistingOutputWithinMaxSize() throws Exception {
        // given
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(1);
        ByteBuffer source = ByteBuffer.wrap(new byte[] {2, 3});

        // when
        ByteArrayOutputStream actual = MethodInvoker.create()
            .object(instance)
            .method("drainBuffer")
            .param(ByteArrayOutputStream.class, output)
            .param(ByteBuffer.class, source)
            .param(int.class, 3)
            .call();

        // then
        Asserts.assertTrue(actual == output);
        Asserts.assertEquals(actual.toByteArray(), new byte[] {1, 2, 3});
    }

    @Test
    public void readCaseBufferOverFlow() throws Exception {
        // given
        beforeNotHandshakeMethod();

        buffer.clear();
        buffer.put(new byte[] {1, 2});
        buffer.flip();
        SSLEngineResult resultBufferOverflow = new SSLEngineResult(
            SSLEngineResult.Status.BUFFER_OVERFLOW,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );
        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenReturn(resultBufferOverflow);

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.read(buffer)
        );

        // then
        Asserts.assertEqualsType(e, EzyConnectionCloseException.class);

        verify(sslEngine, times(2))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readCaseBufferOverFlowButNetBufferEnough() throws Exception {
        // given
        beforeNotHandshakeMethod();
        netBuffer = ByteBuffer.allocate(bufferSize / 2);
        FieldUtil.setFieldValue(instance, "netBuffer", netBuffer);

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
                tcpNetBuffer.put(netBuffer);
                return resultOk;
            });

        // when
        byte[] actual = instance.read(buffer);

        // then
        Asserts.assertEquals(actual, new byte[] {1, 2});

        verify(sslEngine, times(2))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readCaseBufferOverFlowButMaxSize() throws Exception {
        // given
        beforeNotHandshakeMethod();
        FieldUtil.setFieldValue(instance, "sslMaxAppBufferSize", bufferSize/2);

        buffer.clear();
        buffer.put(new byte[] {1, 2});
        buffer.flip();
        SSLEngineResult resultBufferOverflow = new SSLEngineResult(
            SSLEngineResult.Status.BUFFER_OVERFLOW,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );
        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenReturn(resultBufferOverflow);

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.read(buffer)
        );

        // then
        Asserts.assertEqualsType(e, EzyConnectionCloseException.class);

        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readCaseBufferUnderFlow() throws Exception {
        // given
        beforeNotHandshakeMethod();

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
                tcpNetBuffer.put(netBuffer);
                return resultOk;
            });

        // when
        byte[] actual = instance.read(buffer);

        // then
        Asserts.assertEquals(actual, new byte[0]);

        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readCaseBufferClosed() throws Exception {
        // given
        beforeNotHandshakeMethod();
        netBuffer.put((byte) 1);
        netBuffer.flip();
        netBuffer.get();

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
           instance.read(buffer)
        );

        // then
        Asserts.assertEqualsType(e, EzyConnectionCloseException.class);

        verify(sslEngine, times(1)).closeOutbound();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readCaseBufferClosedButException() throws Exception {
        // given
        beforeNotHandshakeMethod();
        netBuffer.put((byte) 1);
        netBuffer.flip();
        netBuffer.get();

        SSLEngineResult result = new SSLEngineResult(
            SSLEngineResult.Status.CLOSED,
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
            0,
            0
        );

        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class)))
            .thenReturn(result);
        RuntimeException exception = new RuntimeException("test");
        doThrow(exception).when(sslEngine).closeOutbound();

        // when
        Throwable e = Asserts.assertThrows(() ->
            instance.read(buffer)
        );

        // then
        Asserts.assertEqualsType(e, EzyConnectionCloseException.class);

        verify(sslEngine, times(1)).closeOutbound();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }

    @Test
    public void readNoRemaining() throws Exception {
        // given
        beforeNotHandshakeMethod();

        buffer.clear();
        buffer.flip();

        // when
        byte[] actual = instance.read(buffer);

        // then
        Asserts.assertEquals(actual, new byte[0]);
    }

    @Test
    public void closeNormalCase() {
        // given
        FieldUtil.setFieldValue(instance, "engine", sslEngine);

        // when
        instance.close();

        // then
        verify(sslEngine, times(1)).closeOutbound();
    }

    @Test
    public void closeBeforeHandshakeCase() {
        // given
        // when
        // then
        instance.close();
    }
}
