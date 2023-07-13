package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.ezyfox.io.EzyByteBuffers;
import com.tvd12.ezyfox.util.EzyThreads;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeHandler;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.*;
import java.io.IOException;
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
    private final int bufferSize = 128;
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
        when(sslSession.getApplicationBufferSize()).thenReturn(bufferSize);
        when(sslSession.getPacketBufferSize()).thenReturn(bufferSize);
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
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

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

        SSLEngineResult engineResult = new SSLEngineResult(
            SSLEngineResult.Status.OK,
            SSLEngineResult.HandshakeStatus.FINISHED,
            0,
            0
        );
        SSLException error = new SSLException("test");
        when(
            sslEngine.wrap(any(ByteBuffer.class), any(ByteBuffer.class))
        ).thenThrow(error);

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

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
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

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
            instance.handle(socketChannel)
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);

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
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

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
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(socketChannel, times(1))
            .write(any(ByteBuffer.class));

        verify(sslEngine, times(2)).getHandshakeStatus();
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
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

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
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

        verify(sslEngine, times(1)).getHandshakeStatus();
    }

    @Test
    public void handleCaseHandshakeStatusIsNotHandshake() throws Exception {
        // given
        when(sslEngine.getHandshakeStatus()).thenReturn(
            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING
        );

        // when
        SSLEngine actual = instance.handle(socketChannel);

        // then
        Asserts.assertEquals(actual, sslEngine);

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
            EzyThreads.sleep(timeout * 2);
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
            instance.handle(socketChannel)
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);

        verify(socketChannel, times(1))
            .read(any(ByteBuffer.class));

        verify(sslEngine, times(1)).getHandshakeStatus();
        verify(sslEngine, times(1))
            .unwrap(any(ByteBuffer.class), any(ByteBuffer.class));
    }
}
