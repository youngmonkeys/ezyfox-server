package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.ezyfox.function.EzyExceptionApply;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.*;

public class EzySslContextProxyTest {

    @Test
    public void loadSslContextTest() {
        // given
        SSLContext sslContext = mock(SSLContext.class);
        EzySslContextProxy instance = new EzySslContextProxy(
            () -> sslContext
        );

        // when
        SSLContext actual = instance.loadSslContext();

        // then
        Asserts.assertEquals(actual, sslContext);
        verifyNoMoreInteractions(sslContext);
    }

    @Test
    public void loadSslContextCachedTest() {
        // given
        SSLContext sslContext = mock(SSLContext.class);
        AtomicInteger supplyCount = new AtomicInteger();
        EzySslContextProxy instance = new EzySslContextProxy(() -> {
            supplyCount.incrementAndGet();
            return sslContext;
        });

        // when
        SSLContext actual1 = instance.loadSslContext();
        SSLContext actual2 = instance.loadSslContext();

        // then
        Asserts.assertEquals(actual1, sslContext);
        Asserts.assertEquals(actual2, sslContext);
        Asserts.assertEquals(supplyCount.get(), 1);
        verifyNoMoreInteractions(sslContext);
    }

    @Test
    public void loadSslContextAfterReloadTest() {
        // given
        SSLContext sslContext1 = mock(SSLContext.class);
        SSLContext sslContext2 = mock(SSLContext.class);
        AtomicInteger supplyCount = new AtomicInteger();
        EzySslContextProxy instance = new EzySslContextProxy(() ->
            supplyCount.incrementAndGet() == 1 ? sslContext1 : sslContext2
        );
        SSLContext before = instance.loadSslContext();

        // when
        instance.reloadSsl();
        SSLContext after = instance.loadSslContext();

        // then
        Asserts.assertEquals(before, sslContext1);
        Asserts.assertEquals(after, sslContext2);
        Asserts.assertEquals(supplyCount.get(), 2);
        verifyNoMoreInteractions(sslContext1);
        verifyNoMoreInteractions(sslContext2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void reloadSslTest() throws Exception {
        // given
        SSLContext sslContext = mock(SSLContext.class);
        EzySslContextProxy instance = new EzySslContextProxy(
            () -> sslContext
        );
        EzyExceptionApply<SSLContext> listener = mock(
            EzyExceptionApply.class
        );
        instance.onSslContextReload(listener);

        // when
        instance.reloadSsl();

        // then
        verify(listener, times(1)).apply(sslContext);
        verifyNoMoreInteractions(listener);
        verifyNoMoreInteractions(sslContext);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void reloadSslExceptionTest() throws Exception {
        // given
        SSLContext sslContext = mock(SSLContext.class);
        EzySslContextProxy instance = new EzySslContextProxy(
            () -> sslContext
        );
        EzyExceptionApply<SSLContext> listener = mock(
            EzyExceptionApply.class
        );
        doThrow(new Exception("test"))
            .when(listener)
            .apply(sslContext);
        instance.onSslContextReload(listener);

        // when
        instance.reloadSsl();

        // then
        verify(listener, times(1)).apply(sslContext);
        verifyNoMoreInteractions(listener);
        verifyNoMoreInteractions(sslContext);
    }

    @Test
    public void loadSslContextSetByOtherThreadWhileWaitingLockTest() throws Exception {
        // given
        SSLContext sslContext = mock(SSLContext.class);
        AtomicInteger supplyCount = new AtomicInteger();
        CountDownLatch supplying = new CountDownLatch(1);
        CountDownLatch releaseSupplier = new CountDownLatch(1);
        EzySslContextProxy instance = new EzySslContextProxy(() -> {
            supplyCount.incrementAndGet();
            supplying.countDown();
            try {
                releaseSupplier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return sslContext;
        });
        AtomicReference<SSLContext> firstResult = new AtomicReference<>();
        AtomicReference<SSLContext> secondResult = new AtomicReference<>();
        Thread firstThread = new Thread(() -> firstResult.set(instance.loadSslContext()));
        Thread secondThread = new Thread(() -> secondResult.set(instance.loadSslContext()));

        // when
        firstThread.start();
        Asserts.assertTrue(supplying.await(5, TimeUnit.SECONDS));
        secondThread.start();
        long deadline = System.currentTimeMillis() + 5000;
        while (secondThread.getState() != Thread.State.BLOCKED
            && System.currentTimeMillis() < deadline
        ) {
            Thread.yield();
        }
        Thread.State secondThreadState = secondThread.getState();
        releaseSupplier.countDown();
        firstThread.join(5000);
        secondThread.join(5000);

        // then
        Asserts.assertEquals(secondThreadState, Thread.State.BLOCKED);
        Asserts.assertEquals(firstResult.get(), sslContext);
        Asserts.assertEquals(secondResult.get(), sslContext);
        Asserts.assertEquals(supplyCount.get(), 1);
        verifyNoMoreInteractions(sslContext);
    }
}
