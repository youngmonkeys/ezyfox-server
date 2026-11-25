package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketChannel;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EzyNioSecureSocketChannelWaitForReadableTest {

    private ServerSocketChannel serverChannel;
    private SocketChannel clientChannel;
    private SocketChannel acceptedChannel;
    private EzyNioSecureSocketChannel instance;

    @BeforeMethod
    public void setup() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("127.0.0.1", 0));
        clientChannel = SocketChannel.open(
            new InetSocketAddress("127.0.0.1", serverChannel.socket().getLocalPort())
        );
        acceptedChannel = serverChannel.accept();
        instance = new EzyNioSecureSocketChannel(
            acceptedChannel,
            new EzySslContextProxy(() -> null),
            1000,
            128
        );
    }

    @AfterMethod
    public void tearDown() {
        processWithLogException(acceptedChannel::close);
        processWithLogException(clientChannel::close);
        processWithLogException(serverChannel::close);
    }

    @Test
    public void waitUntilDataArrivedTest() throws Exception {
        acceptedChannel.configureBlocking(false);
        Thread writer = new Thread(() -> processWithLogException(() -> {
            Thread.sleep(100);
            clientChannel.write(ByteBuffer.wrap(new byte[] {1, 2, 3}));
        }));
        writer.start();

        long startTime = System.currentTimeMillis();
        waitForReadable(startTime + 5000);
        long elapsedTime = System.currentTimeMillis() - startTime;
        writer.join();

        Asserts.assertTrue(elapsedTime >= 50);
        Asserts.assertTrue(elapsedTime < 5000);
        Asserts.assertEquals(acceptedChannel.read(ByteBuffer.allocate(3)), 3);
    }

    @Test
    public void waitUntilTimeoutTest() throws Exception {
        acceptedChannel.configureBlocking(false);

        long startTime = System.currentTimeMillis();
        waitForReadable(startTime + 100);
        long elapsedTime = System.currentTimeMillis() - startTime;

        Asserts.assertTrue(elapsedTime >= 50);
        Asserts.assertTrue(elapsedTime < 5000);
    }

    @Test
    public void notWaitWhenTimeoutPassedTest() throws Exception {
        acceptedChannel.configureBlocking(false);

        long startTime = System.currentTimeMillis();
        waitForReadable(startTime - 1);
        long elapsedTime = System.currentTimeMillis() - startTime;

        Asserts.assertTrue(elapsedTime < 50);
    }

    @Test
    public void notWaitWhenBlockingTest() throws Exception {
        long startTime = System.currentTimeMillis();
        waitForReadable(startTime + 5000);
        long elapsedTime = System.currentTimeMillis() - startTime;

        Asserts.assertTrue(elapsedTime < 1000);
    }

    @Test
    public void channelStillUsableAfterWaitTest() throws Exception {
        acceptedChannel.configureBlocking(false);
        clientChannel.write(ByteBuffer.wrap(new byte[] {1}));

        waitForReadable(System.currentTimeMillis() + 5000);
        waitForReadable(System.currentTimeMillis() + 5000);

        Asserts.assertEquals(acceptedChannel.read(ByteBuffer.allocate(1)), 1);
        acceptedChannel.configureBlocking(true);
        Asserts.assertTrue(acceptedChannel.isBlocking());
    }

    @Test
    public void handshakeBufferUnderflowNoReadBytesWaitUntilDataArrivedTest() throws Exception {
        // given
        acceptedChannel.configureBlocking(false);
        SSLEngine sslEngine = mockSslEngine();
        AtomicInteger unwrapCallCount = new AtomicInteger();
        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))).thenAnswer(it -> {
            if (unwrapCallCount.incrementAndGet() == 1) {
                return new SSLEngineResult(
                    SSLEngineResult.Status.BUFFER_UNDERFLOW,
                    SSLEngineResult.HandshakeStatus.NEED_UNWRAP,
                    0,
                    0
                );
            }
            return new SSLEngineResult(
                SSLEngineResult.Status.OK,
                SSLEngineResult.HandshakeStatus.FINISHED,
                0,
                0
            );
        });
        EzyNioSecureSocketChannel sut = newSecureChannel(sslEngine, 5000);
        Thread writer = new Thread(() -> processWithLogException(() -> {
            Thread.sleep(100);
            clientChannel.write(ByteBuffer.wrap(new byte[] {1, 2, 3}));
        }));

        // when
        writer.start();
        long startTime = System.currentTimeMillis();
        sut.handshake();
        long elapsedTime = System.currentTimeMillis() - startTime;
        writer.join();

        // then
        Asserts.assertTrue(sut.isHandshakeComplete());
        Asserts.assertTrue(elapsedTime >= 50);
        Asserts.assertTrue(elapsedTime < 5000);
        Asserts.assertEquals(unwrapCallCount.get(), 2);
    }

    @Test
    public void handshakeBufferUnderflowNoReadBytesWaitUntilTimeoutTest() throws Exception {
        // given
        acceptedChannel.configureBlocking(false);
        SSLEngine sslEngine = mockSslEngine();
        AtomicInteger unwrapCallCount = new AtomicInteger();
        when(sslEngine.unwrap(any(ByteBuffer.class), any(ByteBuffer.class))).thenAnswer(it -> {
            unwrapCallCount.incrementAndGet();
            return new SSLEngineResult(
                SSLEngineResult.Status.BUFFER_UNDERFLOW,
                SSLEngineResult.HandshakeStatus.NEED_UNWRAP,
                0,
                0
            );
        });
        EzyNioSecureSocketChannel sut = newSecureChannel(sslEngine, 200);

        // when
        long startTime = System.currentTimeMillis();
        Throwable e = Asserts.assertThrows(() -> sut.handshake());
        long elapsedTime = System.currentTimeMillis() - startTime;

        // then
        Asserts.assertEqualsType(e, SSLException.class);
        Asserts.assertEquals(e.getMessage(), "Timeout");
        Asserts.assertFalse(sut.isHandshakeComplete());
        Asserts.assertTrue(elapsedTime >= 150);
        Asserts.assertTrue(elapsedTime < 5000);
        Asserts.assertTrue(unwrapCallCount.get() >= 1);
    }

    private SSLEngine mockSslEngine() {
        SSLEngine sslEngine = mock(SSLEngine.class);
        SSLSession sslSession = mock(SSLSession.class);
        when(sslEngine.getSession()).thenReturn(sslSession);
        when(sslSession.getPacketBufferSize()).thenReturn(128);
        when(sslSession.getApplicationBufferSize()).thenReturn(128);
        when(sslEngine.getHandshakeStatus()).thenReturn(SSLEngineResult.HandshakeStatus.NEED_UNWRAP);
        return sslEngine;
    }

    private EzyNioSecureSocketChannel newSecureChannel(SSLEngine sslEngine, int handshakeTimeout) {
        SSLContext sslContext = mock(SSLContext.class);
        EzySSLContextSpiForTest sslContextSpi = mock(EzySSLContextSpiForTest.class);
        FieldUtil.setFieldValue(sslContext, "contextSpi", sslContextSpi);
        when(sslContextSpi.engineCreateSSLEngine()).thenReturn(sslEngine);
        return new EzyNioSecureSocketChannel(
            acceptedChannel,
            new EzySslContextProxy(() -> sslContext),
            handshakeTimeout,
            128
        );
    }

    private void waitForReadable(long timeoutAt) {
        MethodInvoker.create()
            .object(instance)
            .method("waitForReadable")
            .param(long.class, timeoutAt)
            .invoke();
    }
}
