package com.tvd12.ezyfoxserver.testing.builder;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestServerBootstrapBuilder;

public class EzyAbtractServerBootstrapBuilderTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimpleServer server = newServer();
        MyTestServerBootstrapBuilder builder = 
                (MyTestServerBootstrapBuilder) new MyTestServerBootstrapBuilder()
                .server(server);
        assert !builder.equals(null);
    }
    
}
