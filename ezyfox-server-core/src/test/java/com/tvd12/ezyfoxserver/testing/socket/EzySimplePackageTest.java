package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzySimplePackage;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzySimplePackageTest extends BaseTest {

    @Test
    public void test() {
        EzySimplePackage pack = new EzySimplePackage();
        pack.setTransportType(EzyTransportType.TCP);
        assert pack.getTransportType() == EzyTransportType.TCP;
    }

    @Test
    public void getConnectionTypesTest() {
        EzySession socketSession = mock(EzySession.class);
        when(socketSession.getConnectionType()).thenReturn("SOCKET");
        EzySession websocketSession = mock(EzySession.class);
        when(websocketSession.getConnectionType()).thenReturn("WEBSOCKET");

        EzySimplePackage pack = new EzySimplePackage();
        pack.addRecipients(Arrays.asList(socketSession, websocketSession));

        Set<String> expected = new HashSet<>(Arrays.asList("SOCKET", "WEBSOCKET"));
        assert pack.getConnectionTypes().equals(expected);
    }
}
