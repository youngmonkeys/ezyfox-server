package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfox.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.builder.EzyAbstractServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyStarterTest {

    @Test
    public void normalCaseTest() throws Exception {
        EzyStarter starter = new ExEzyStarter.Builder()
            .configFile("test-data/settings/config.properties")
            .build();
        starter.start();
    }

    @Test
    public void notPrintSettings() {
        // given
        EzyStarter starter = new ExEzyStarter.Builder()
            .build();

        EzyConfig config = mock(EzyConfig.class);
        when(config.isPrintSettings()).thenReturn(false);

        EzySimpleServer server = new EzySimpleServer();
        server.setConfig(config);

        EzySimpleSettings settings = new EzySimpleSettings();
        server.setSettings(settings);

        // when
        MethodInvoker.create()
            .object(starter)
            .method("startEzyFox")
            .param(EzyServer.class, server)
            .call();

        // then
        verify(config, times(1)).isPrintSettings();
    }

    public static class ExEzyStarter extends EzyStarter {

        protected ExEzyStarter(Builder builder) {
            super(builder);
        }

        @Override
        protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
            return new EzyAbstractServerBootstrapBuilder() {

                @Override
                protected EzyServerBootstrap newServerBootstrap() {
                    return new EzyServerBootstrap() {

                        @Override
                        protected void startOtherBootstraps(Runnable callback) {}

                        @Override
                        protected void startLocalBootstrap() {}
                    };
                }
            };
        }

        @Override
        protected EzySimpleSessionManager.Builder newSessionManagerBuilder(
            EzySettings settings) {
            return new ExEzySimpleSessionManager.Builder();
        }

        public static class Builder extends EzyStarter.Builder<Builder> {

            @Override
            public EzyStarter build() {
                return new ExEzyStarter(this);
            }
        }
    }

    public static class ExEzySimpleSessionManager extends EzySimpleSessionManager {

        protected ExEzySimpleSessionManager(Builder builder) {
            super(builder);
        }

        public static class Builder extends EzySimpleSessionManager.Builder {

            @Override
            public EzySimpleSessionManager build() {
                return new ExEzySimpleSessionManager(this);
            }

            @Override
            protected EzyObjectFactory newObjectFactory() {
                return mock(EzyObjectFactory.class);
            }
        }
    }
}

