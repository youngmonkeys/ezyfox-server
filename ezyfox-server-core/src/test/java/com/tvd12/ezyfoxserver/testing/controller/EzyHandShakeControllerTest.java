package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.sercurity.EzyBase64;
import com.tvd12.ezyfox.sercurity.EzyKeysGenerator;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyHandshakeController;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.request.EzyHandshakeParams;
import com.tvd12.ezyfoxserver.request.EzySimpleHandshakeRequest;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.performance.Performance;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.security.KeyPair;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EzyHandShakeControllerTest extends EzyBaseControllerTest {

    @Test
    public void test() {
        EzyServerContext ctx = newServerContext();
        EzySession session = newSession();
        EzyArray data = newHandShakeData();
        EzyHandshakeController controller = new EzyHandshakeController();
        EzySimpleHandshakeRequest request = new EzySimpleHandshakeRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }

    @Test
    public void testDeserializeParamsPerformance() {
        EzyArray data = newHandShakeData();
        long time = Performance.create()
            .test(() -> {
                EzySimpleHandshakeRequest request = new EzySimpleHandshakeRequest();
                request.deserializeParams(data);
            })
            .getTime();
        System.out.println("testDeserializeParamsPerformance, time = " + time);

    }

    @Test
    public void handleSocketSSLTest() throws Exception {
        // given
        EzyHandshakeController sut = new EzyHandshakeController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyHandShakeRequest request = mock(EzyHandShakeRequest.class);

        EzyHandshakeParams params = mock(EzyHandshakeParams.class);
        when(request.getParams()).thenReturn(params);

        EzySession session = spy(EzyAbstractSession.class);
        when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        when(request.getSession()).thenReturn(session);

        EzyServer server = mock(EzyServer.class);
        EzySettings settings = mock(EzySettings.class);
        EzySocketSetting socketSetting = mock(EzySocketSetting.class);
        when(settings.getSocket()).thenReturn(socketSetting);
        when(socketSetting.isSslActive()).thenReturn(true);
        when(serverContext.getServer()).thenReturn(server);
        when(server.getSettings()).thenReturn(settings);

        String clientId = RandomUtil.randomShortHexString();
        String clientType = RandomUtil.randomShortAlphabetString();
        String clientVersion = RandomUtil.randomShortAlphabetString();
        String reconnectToken = RandomUtil.randomShortHexString();
        KeyPair keyPair = EzyKeysGenerator.builder()
            .build()
            .generate();
        byte[] clientKey = keyPair.getPublic().getEncoded();
        when(params.getClientId()).thenReturn(clientId);
        when(params.getClientKey()).thenReturn(clientKey);
        when(params.getClientType()).thenReturn(clientType);
        when(params.getClientVersion()).thenReturn(clientVersion);
        when(params.getReconnectToken()).thenReturn(reconnectToken);
        when(params.isEnableEncryption()).thenReturn(true);

        // when
        sut.handle(serverContext, request);


        // then
        verify(session, times(1)).setClientId(clientId);
        verify(session, times(1)).setClientKey(clientKey);
        verify(session, times(1)).setClientType(clientType);
        verify(session, times(1)).setClientVersion(clientVersion);
        verify(session, times(1)).setSessionKey(any(byte[].class));
        Asserts.assertNotNull(session.getSessionKey());
    }

    @Test
    public void handleSocketSSLButWebsocketTest() {
        // given
        EzyHandshakeController sut = new EzyHandshakeController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyHandShakeRequest request = mock(EzyHandShakeRequest.class);

        EzyHandshakeParams params = mock(EzyHandshakeParams.class);
        when(request.getParams()).thenReturn(params);

        EzySession session = spy(EzyAbstractSession.class);
        when(session.getConnectionType()).thenReturn(EzyConnectionType.WEBSOCKET);
        when(request.getSession()).thenReturn(session);

        // when
        sut.handle(serverContext, request);


        // then
        Asserts.assertNull(session.getSessionKey());
    }

    @Test
    public void handleSocketSSLButEventNoEncryptionTest() {
        // given
        EzyHandshakeController sut = new EzyHandshakeController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyHandShakeRequest request = mock(EzyHandShakeRequest.class);

        EzyHandshakeParams params = mock(EzyHandshakeParams.class);
        when(request.getParams()).thenReturn(params);

        EzySession session = spy(EzyAbstractSession.class);
        when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        when(request.getSession()).thenReturn(session);

        EzyServer server = mock(EzyServer.class);
        EzySettings settings = mock(EzySettings.class);
        EzySocketSetting socketSetting = mock(EzySocketSetting.class);
        when(settings.getSocket()).thenReturn(socketSetting);
        when(socketSetting.isSslActive()).thenReturn(true);
        when(serverContext.getServer()).thenReturn(server);
        when(server.getSettings()).thenReturn(settings);

        when(params.isEnableEncryption()).thenReturn(false);

        // when
        sut.handle(serverContext, request);


        // then
        Asserts.assertNull(session.getSessionKey());
    }

    @Test
    public void handleSocketSSLButClientKeyEmptyTest() {
        // given
        EzyHandshakeController sut = new EzyHandshakeController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyHandShakeRequest request = mock(EzyHandShakeRequest.class);

        EzyHandshakeParams params = mock(EzyHandshakeParams.class);
        when(request.getParams()).thenReturn(params);

        EzySession session = spy(EzyAbstractSession.class);
        when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        when(request.getSession()).thenReturn(session);

        EzyServer server = mock(EzyServer.class);
        EzySettings settings = mock(EzySettings.class);
        EzySocketSetting socketSetting = mock(EzySocketSetting.class);
        when(settings.getSocket()).thenReturn(socketSetting);
        when(socketSetting.isSslActive()).thenReturn(true);
        when(serverContext.getServer()).thenReturn(server);
        when(server.getSettings()).thenReturn(settings);

        String clientId = RandomUtil.randomShortHexString();
        String clientType = RandomUtil.randomShortAlphabetString();
        String clientVersion = RandomUtil.randomShortAlphabetString();
        String reconnectToken = RandomUtil.randomShortHexString();
        byte[] clientKey = new byte[0];
        when(params.getClientId()).thenReturn(clientId);
        when(params.getClientKey()).thenReturn(clientKey);
        when(params.getClientType()).thenReturn(clientType);
        when(params.getClientVersion()).thenReturn(clientVersion);
        when(params.getReconnectToken()).thenReturn(reconnectToken);
        when(params.isEnableEncryption()).thenReturn(true);

        // when
        sut.handle(serverContext, request);


        // then
        verify(session, times(1)).setClientId(clientId);
        verify(session, times(1)).setClientKey(clientKey);
        verify(session, times(1)).setClientType(clientType);
        verify(session, times(1)).setClientVersion(clientVersion);
        verify(session, times(1)).setSessionKey(any(byte[].class));
    }

    @Test
    public void handleSocketSSLButInvalidClientKeyEmptyTest() {
        // given
        EzyHandshakeController sut = new EzyHandshakeController();
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzyHandShakeRequest request = mock(EzyHandShakeRequest.class);

        EzyHandshakeParams params = mock(EzyHandshakeParams.class);
        when(request.getParams()).thenReturn(params);

        EzySession session = spy(EzyAbstractSession.class);
        when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
        when(request.getSession()).thenReturn(session);

        EzyServer server = mock(EzyServer.class);
        EzySettings settings = mock(EzySettings.class);
        EzySocketSetting socketSetting = mock(EzySocketSetting.class);
        when(settings.getSocket()).thenReturn(socketSetting);
        when(socketSetting.isSslActive()).thenReturn(true);
        when(serverContext.getServer()).thenReturn(server);
        when(server.getSettings()).thenReturn(settings);

        String clientId = RandomUtil.randomShortHexString();
        String clientType = RandomUtil.randomShortAlphabetString();
        String clientVersion = RandomUtil.randomShortAlphabetString();
        String reconnectToken = RandomUtil.randomShortHexString();
        byte[] clientKey = new byte[]{1, 2, 3};
        when(params.getClientId()).thenReturn(clientId);
        when(params.getClientKey()).thenReturn(clientKey);
        when(params.getClientType()).thenReturn(clientType);
        when(params.getClientVersion()).thenReturn(clientVersion);
        when(params.getReconnectToken()).thenReturn(reconnectToken);
        when(params.isEnableEncryption()).thenReturn(true);

        // when
        sut.handle(serverContext, request);


        // then
        verify(session, times(1)).setClientId(clientId);
        verify(session, times(1)).setClientKey(clientKey);
        verify(session, times(1)).setClientType(clientType);
        verify(session, times(1)).setClientVersion(clientVersion);
        verify(session, times(1)).setSessionKey(any(byte[].class));
    }

    private EzyArray newHandShakeData() {
        KeyPair keyPair = newRSAKeys();
        return newArrayBuilder()
            .append("adroid#1")
            .append(EzyBase64.encode2utf(keyPair.getPublic().getEncoded()))
            .append("android")
            .append("1.0.0")
            .append(true)
            .append("reconnectToken#1")
            .build();
    }

    @Override
    protected EzySession newSession() {
        KeyPair keyPair = newRSAKeys();
        EzySession session = super.newSession();
        session.setToken("reconnectToken#1");
        session.setPublicKey(keyPair.getPublic().getEncoded());
        return session;
    }

    @Override
    protected EzyConstant getCommand() {
        return EzyCommand.HANDSHAKE;
    }

}
