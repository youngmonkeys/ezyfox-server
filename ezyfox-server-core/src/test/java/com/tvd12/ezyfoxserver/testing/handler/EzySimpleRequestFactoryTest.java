package com.tvd12.ezyfoxserver.testing.handler;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.handler.EzySimpleRequestFactory;
import org.testng.annotations.Test;

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
