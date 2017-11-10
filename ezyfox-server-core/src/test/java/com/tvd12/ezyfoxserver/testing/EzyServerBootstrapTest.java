package com.tvd12.ezyfoxserver.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzyServerBootstrap;

public class EzyServerBootstrapTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyServerBootstrap bt = new MyTestServerBootstrapBuilder()
            .server(newServer())
            .build();
        bt.destroy();
    }
    
}
