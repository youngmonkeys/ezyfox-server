package com.tvd12.ezyfoxserver.nio.testing.builder;

import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketSecureServerCreator;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandler;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;

public class EzyWebSocketSecureServerCreatorTest extends BaseTest {

    @Test
    public void test() throws Exception {
        SSLContext sslContext = SSLContext.getDefault();
        EzySslContextProxy sslContextProxy = new EzySslContextProxy(
            () -> sslContext
        );
        EzySimpleWebSocketSetting webSocketSetting = new EzySimpleWebSocketSetting();
        EzyWebSocketSecureServerCreator creator = new EzyWebSocketSecureServerCreator(
            sslContextProxy
        );
        creator.setting(webSocketSetting);
        creator.create();

        EzyWsHandler wsHandler = MethodInvoker.create()
            .object(creator)
            .method("newWsHandler")
            .invoke(EzyWsHandler.class);

        WebSocketCreator webSocketCreator = MethodInvoker.create()
            .object(creator)
            .method("newWebSocketCreator")
            .param(EzyWsHandler.class, wsHandler)
            .invoke(WebSocketCreator.class);
        webSocketCreator.createWebSocket(null, null);
        sslContextProxy.reloadSsl();
    }
}
