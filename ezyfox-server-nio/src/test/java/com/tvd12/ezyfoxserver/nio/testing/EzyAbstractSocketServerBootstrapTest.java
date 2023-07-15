package com.tvd12.ezyfoxserver.nio.testing;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.EzyAbstractSocketServerBootstrap;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.test.reflect.FieldUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyAbstractSocketServerBootstrapTest {

    private EzyServer server;
    private EzySettings settings;
    private EzyServerContext serverContext;

    @BeforeMethod
    public void setup() {
        serverContext = mock(EzyServerContext.class);
        server = mock(EzyServer.class);
        settings = mock(EzySettings.class);

        when(serverContext.getServer()).thenReturn(server);
        when(server.getSettings()).thenReturn(settings);
    }

    @AfterMethod
    public void verifyAll() {
        verify(serverContext, times(1)).getServer();
        verifyNoMoreInteractions(serverContext);

        verify(server, times(1)).getSettings();
        verifyNoMoreInteractions(server);

        verifyNoMoreInteractions(settings);
    }

    @Test
    public void destroyTest() {
        // given
        EzySocketEventLoopHandler writingLoopHandler = mock(EzySocketEventLoopHandler.class);
        InternalBoostrap sut = new InternalBoostrap.Builder()
            .serverContext(serverContext)
            .build();
        FieldUtil.setFieldValue(sut, "writingLoopHandler", writingLoopHandler);

        // when
        sut.destroy();

        // then
        verify(writingLoopHandler, times(1)).destroy();
    }

    private static class InternalBoostrap extends EzyAbstractSocketServerBootstrap {

        public InternalBoostrap(Builder builder) {
            super(builder);
        }

        @Override
        public void start() throws Exception {
        }

        public static class Builder extends EzyAbstractSocketServerBootstrap.Builder<Builder, InternalBoostrap> {
            @Override
            public InternalBoostrap build() {
                return new InternalBoostrap(this);
            }
        }
    }
}
