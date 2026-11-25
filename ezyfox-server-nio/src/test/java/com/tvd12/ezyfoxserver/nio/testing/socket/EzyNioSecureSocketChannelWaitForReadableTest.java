package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketChannel;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

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

    private void waitForReadable(long timeoutAt) {
        MethodInvoker.create()
            .object(instance)
            .method("waitForReadable")
            .param(long.class, timeoutAt)
            .invoke();
    }
}
