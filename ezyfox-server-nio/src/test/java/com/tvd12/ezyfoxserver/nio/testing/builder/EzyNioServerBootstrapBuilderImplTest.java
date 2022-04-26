package com.tvd12.ezyfoxserver.nio.testing.builder;

import com.tvd12.ezyfox.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyNioServerBootstrapBuilderImpl;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyNioServerBootstrapBuilderImplTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleConfig config = new EzySimpleConfig();
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.getSocket().setCodecCreator(ExCodecCreator.class.getName());
        settings.getWebsocket().setCodecCreator(ExCodecCreator.class.getName());
        EzySimpleServer server = new EzySimpleServer();
        server.setConfig(config);
        server.setSettings(settings);
        EzyNioServerBootstrapBuilderImpl builder = new EzyNioServerBootstrapBuilderImpl();
        builder.server(server);
        builder.build();
    }

    public static class ExCodecCreator implements EzyCodecCreator {

        @Override
        public Object newEncoder() {
            return null;
        }

        @Override
        public Object newDecoder(int maxRequestSize) {
            return null;
        }
    }
}
