package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.ezyfox.function.EzyExceptionApply;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import java.util.concurrent.atomic.AtomicInteger;

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
}
