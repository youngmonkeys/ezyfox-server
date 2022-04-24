package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class EzyServerTest extends BaseCoreTest {

    private EzySimpleServer server;

    public EzyServerTest() {
        super();
        server = newServer();
    }

    @Test
    public void test() {
        assertEquals(server.getClassLoader(), EzySimpleServer.class.getClassLoader());
        assertEquals(server.getControllers() != null, true);
        assertEquals(server.getAppClassLoaders().containsKey("ezyfox-chat"), true);
    }
}
