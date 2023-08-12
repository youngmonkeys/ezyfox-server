package com.tvd12.ezyfoxserver.nio.testing;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.EzySocketServerBootstrap;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSecureSocketAcceptor;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketAcceptor;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;

import static org.mockito.Mockito.*;

public class EzySocketServerBootstrapTest {

    private EzyServer server;
    private EzySettings settings;
    private EzySocketSetting socketSetting;
    private SSLContext sslContext;
    private EzyServerContext serverContext;
    private EzySocketServerBootstrap instance;

    @BeforeMethod
    public void setup() {
        sslContext = mock(SSLContext.class);
        serverContext = mock(EzyServerContext.class);
        server = mock(EzyServer.class);
        settings = mock(EzySettings.class);
        socketSetting = mock(EzySocketSetting.class);

        when(serverContext.getServer()).thenReturn(server);
        when(server.getSettings()).thenReturn(settings);
        when(settings.getSocket()).thenReturn(socketSetting);

        instance = EzySocketServerBootstrap.builder()
            .sslContext(sslContext)
            .serverContext(serverContext)
            .build();
    }

    @AfterMethod
    public void verifyAll() {
        verify(serverContext, times(1)).getServer();
        verifyNoMoreInteractions(serverContext);

        verify(server, times(1)).getSettings();
        verifyNoMoreInteractions(server);

        verify(settings, times(1)).getSocket();
        verifyNoMoreInteractions(settings);

        verify(socketSetting, times(1)).isCertificationSslActive();
        verify(socketSetting, times(1)).getSslHandshakeTimeout();
        verify(socketSetting, times(1)).getMaxRequestSize();
        verifyNoMoreInteractions(socketSetting);
        verifyNoMoreInteractions(sslContext);
    }

    @Test
    public void newSocketAcceptorCaseCertificationSsl() {
        // given
        when(socketSetting.isCertificationSslActive()).thenReturn(true);

        // when
        EzyNioSocketAcceptor acceptor = MethodInvoker.create()
            .object(instance)
            .method("newSocketAcceptor")
            .invoke(EzyNioSocketAcceptor.class);

        // then
        Asserts.assertEqualsType(acceptor, EzyNioSecureSocketAcceptor.class);

        verify(socketSetting, times(1)).isCertificationSslActive();
        verify(socketSetting, times(1)).getSslHandshakeTimeout();
    }
}
