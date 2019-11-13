package com.tvd12.ezyfoxserver.testing.handler;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.handler.EzySimpleRequestFactory;

public class EzySimpleRequestFactoryTest {

    @Test
    public void test() {
        EzySimpleRequestFactory factory = new EzySimpleRequestFactory();
        factory.newRequest(EzyCommand.PING);
        factory.newRequest(EzyCommand.APP_EXIT);
        factory.newRequest(EzyCommand.PLUGIN_INFO);
        factory.newRequest(EzyCommand.PLUGIN_REQUEST);
    }
    
}
