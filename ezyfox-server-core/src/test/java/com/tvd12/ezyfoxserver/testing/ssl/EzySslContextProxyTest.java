package com.tvd12.ezyfoxserver.testing.ssl;

import com.tvd12.ezyfox.function.EzyExceptionApply;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;

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
