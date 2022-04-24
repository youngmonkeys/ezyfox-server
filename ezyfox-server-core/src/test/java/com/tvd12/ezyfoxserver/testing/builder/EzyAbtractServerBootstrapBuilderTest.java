package com.tvd12.ezyfoxserver.testing.builder;

import javax.net.ssl.SSLContext;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.setting.EzySimpleSslConfigSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySslConfigSetting;
import com.tvd12.ezyfoxserver.setting.EzyThreadPoolSizeSetting;
import com.tvd12.ezyfoxserver.setting.EzyUdpSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestServerBootstrapBuilder;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.reflect.ReflectMethodUtil;

public class EzyAbtractServerBootstrapBuilderTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimpleServer server = newServer();
        MyTestServerBootstrapBuilder builder = 
                (MyTestServerBootstrapBuilder) new MyTestServerBootstrapBuilder()
                .server(server);
        assert !builder.equals(null);
        
        EzySslConfigSetting sslConfigSetting = server.getSettings()
                .getWebsocket()
                .getSslConfig();
        
        MethodInvoker.create()
            .object(builder)
            .method("newSslContext")
            .param(EzySslConfigSetting.class, sslConfigSetting)
            .invoke();
        
        MethodInvoker.create()
            .object(builder)
            .method("getSettings")
            .invoke();
        
        MethodInvoker.create()
            .object(builder)
            .method("getSocketSetting")
            .invoke();
        
        MethodInvoker.create()
            .object(builder)
            .method("getWebsocketSetting")
            .invoke();
    }
    
    @Test
    public void commonTest() {
        // given
        EzySimpleServer server = newServer();
        MyTestServerBootstrapBuilder builder = 
                (MyTestServerBootstrapBuilder) new MyTestServerBootstrapBuilder()
                .server(server);

        // when
        EzyUdpSetting udpSetting = 
                (EzyUdpSetting) ReflectMethodUtil.invokeMethod("getUdpSetting", builder);
        EzyThreadPoolSizeSetting threadPoolSizeSetting =
                (EzyThreadPoolSizeSetting) ReflectMethodUtil.invokeMethod("getThreadPoolSizeSetting", builder);

        // then
        Asserts.assertEquals(server.getSettings().getUdp(), udpSetting);
        Asserts.assertEquals(server.getSettings().getThreadPoolSize(), threadPoolSizeSetting);
    }
    
    @Test
    public void newSslContextTest() {
        // given
        EzySimpleServer server = newServer();
        EzySimpleWebSocketSetting webSocketSetting =
                (EzySimpleWebSocketSetting)server.getSettings().getWebsocket();
        webSocketSetting.setSslActive(true);

        MyTestServerBootstrapBuilder builder =
                (MyTestServerBootstrapBuilder) new MyTestServerBootstrapBuilder()
                .server(server);

        EzySimpleSslConfigSetting setting = new EzySimpleSslConfigSetting();

        // when
        SSLContext sslContext = MethodInvoker.create()
            .object(builder)
            .method("newSslContext")
            .param(EzySslConfigSetting.class, setting)
            .invoke(SSLContext.class);

        Asserts.assertNotNull(sslContext);
    }
}
