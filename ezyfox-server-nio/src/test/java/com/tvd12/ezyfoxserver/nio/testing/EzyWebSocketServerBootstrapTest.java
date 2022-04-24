package com.tvd12.ezyfoxserver.nio.testing;

import javax.net.ssl.SSLContext;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.EzyWebSocketServerBootstrap;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketServerCreator;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodUtil;

import static org.mockito.Mockito.*;

public class EzyWebSocketServerBootstrapTest {

    @Test
    public void newSocketServerCreatorWithSSL() {
        // given
        SSLContext sslContext = mock(SSLContext.class);

        EzyServerContext serverContext = mock(EzyServerContext.class);

        EzySimpleServer server = new EzySimpleServer();
        when(serverContext.getServer()).thenReturn(server);

        EzySimpleSettings settings = new EzySimpleSettings();
        settings.getWebsocket().setSslActive(true);
        server.setSettings(settings);

        EzyWebSocketServerBootstrap sut = EzyWebSocketServerBootstrap.builder()
                .sslContext(sslContext)
                .serverContext(serverContext)
                .build();

        // when
        EzyWebSocketServerCreator creator = MethodUtil.invokeMethod("newSocketServerCreator", sut);

        // then
        Asserts.assertNotNull(creator);
    }
}
