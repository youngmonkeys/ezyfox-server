package com.tvd12.ezyfoxserver.testing.builder;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.setting.EzySslConfigSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestServerBootstrapBuilder;
import com.tvd12.test.reflect.MethodInvoker;

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
    
}
