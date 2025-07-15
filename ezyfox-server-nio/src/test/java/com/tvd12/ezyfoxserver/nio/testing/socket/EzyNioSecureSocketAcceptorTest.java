package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketAcceptor;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketChannel;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import java.nio.channels.SocketChannel;

import static org.mockito.Mockito.*;

public class EzyNioSecureSocketAcceptorTest {

    private SSLEngine sslEngine;
    private SSLContext sslContext;
    private EzySSLContextSpiForTest sslContextSpi;
    private SSLSession sslSession;
    private SocketChannel socketChannel;
    private EzyNioSecureSocketAcceptor instance;
    private static final int SSL_HANDSHAKE_TIMEOUT = 100;
    private static final int MAX_REQUEST_SIZE = 1024;

    @BeforeMethod
    public void setup() {
        sslContext = mock(SSLContext.class);
        sslContextSpi = mock(EzySSLContextSpiForTest.class);
        sslEngine = mock(SSLEngine.class);
        sslSession = mock(SSLSession.class);
        socketChannel = mock(SocketChannel.class);
        EzySslContextProxy sslContextProxy = new EzySslContextProxy(() ->
            sslContext
        );
        instance = new EzyNioSecureSocketAcceptor(
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

    @Test
    public void newChannelTest() {
        // given
        // when
        EzyChannel channel = MethodInvoker.create()
            .object(instance)
            .method("newChannel")
            .param(SocketChannel.class, socketChannel)
            .invoke(EzyChannel.class);

        // then
        Asserts.assertEqualsType(channel, EzyNioSecureSocketChannel.class);
    }
}
