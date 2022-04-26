package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class EzyServerTest extends BaseCoreTest {

    private final EzySimpleServer server;

    public EzyServerTest() {
        super();
        server = newServer();
    }

    @Test
    public void test() {
        assertEquals(server.getClassLoader(), EzySimpleServer.class.getClassLoader());
        assertNotNull(server.getControllers());
        assertTrue(server.getAppClassLoaders().containsKey("ezyfox-chat"));
    }
}
