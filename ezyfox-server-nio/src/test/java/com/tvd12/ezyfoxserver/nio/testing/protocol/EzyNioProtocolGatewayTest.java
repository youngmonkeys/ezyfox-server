package com.tvd12.ezyfoxserver.nio.testing.protocol;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioDataHandler;
import com.tvd12.ezyfoxserver.nio.protocol.EzyNioProtocolGateway;
import com.tvd12.ezyfoxserver.nio.protocol.EzyProtocolHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolChannel;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class EzyNioProtocolGatewayTest {

    private Object connection;
    private EzySession session;
    private EzyNioSession handlerGroupSession;
    private EzyProtocolHandlerGroup handlerGroup;
    private EzyHandlerGroupManager handlerGroupManager;
    private EzyNioProtocolGateway instance;

    @BeforeMethod
    public void setup() {
        connection = new Object();
        session = mock(EzySession.class);
        handlerGroupSession = mock(EzyNioSession.class);
        handlerGroup = mock(EzyProtocolHandlerGroup.class);
        handlerGroupManager = mock(EzyHandlerGroupManager.class);
        instance = new EzyNioProtocolGateway(handlerGroupManager);

        when(session.getConnection()).thenReturn(connection);
        when(handlerGroup.getSession()).thenReturn(handlerGroupSession);
    }

    @Test
    public void openSessionTest() {
        EzyProtocolChannel channel = mock(EzyProtocolChannel.class);
        when(channel.getConnectionType()).thenReturn("test");
        when(handlerGroupManager.newHandlerGroup(channel, "test"))
            .thenReturn(handlerGroup);

        EzySession actual = instance.openSession(channel);

        Asserts.assertEquals(actual, handlerGroupSession);
        verify(channel, times(1)).getConnectionType();
        verify(handlerGroupManager, times(1)).newHandlerGroup(channel, "test");
        verify(handlerGroup, times(1)).getSession();
        verifyNoMoreInteractions(handlerGroupManager);
    }

    @Test
    public void getSessionTest() {
        when(handlerGroupManager.getHandlerGroup(connection))
            .thenReturn(handlerGroup);

        EzySession actual = instance.getSession(connection);

        Asserts.assertEquals(actual, handlerGroupSession);
        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verify(handlerGroup, times(1)).getSession();
        verifyNoMoreInteractions(handlerGroupManager);
    }

    @Test
    public void getSessionWithHandlerGroupNullTest() {
        EzySession actual = instance.getSession(connection);

        Asserts.assertNull(actual);
        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verifyNoMoreInteractions(handlerGroupManager);
    }

    @Test
    public void receiveTest() {
        when(handlerGroupManager.getHandlerGroup(connection))
            .thenReturn(handlerGroup);
        EzyArray data = EzyEntityFactory.newArray();

        instance.receive(session, data, 100);

        verify(session, times(1)).getConnection();
        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verify(handlerGroup, times(1)).fireDataReceived(data, 100);
        verifyNoMoreInteractions(handlerGroupManager);
    }

    @Test
    public void receiveWithSessionNullTest() {
        EzyArray data = EzyEntityFactory.newArray();

        instance.receive(null, data, 100);

        verifyZeroInteractions(handlerGroupManager);
    }

    @Test
    public void receiveWithConnectionNullTest() {
        when(session.getConnection()).thenReturn(null);
        EzyArray data = EzyEntityFactory.newArray();

        instance.receive(session, data, 100);

        verify(session, times(1)).getConnection();
        verifyZeroInteractions(handlerGroupManager);
    }

    @Test
    public void receiveWithHandlerGroupNullTest() {
        EzyArray data = EzyEntityFactory.newArray();

        instance.receive(session, data, 100);

        verify(session, times(1)).getConnection();
        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verifyNoMoreInteractions(handlerGroupManager);
        verifyZeroInteractions(handlerGroup);
    }

    @Test
    public void receiveCommandTest() {
        when(handlerGroupManager.getHandlerGroup(connection))
            .thenReturn(handlerGroup);
        EzyArray params = EzyEntityFactory.newArrayBuilder()
            .append("app")
            .build();

        instance.receive(session, EzyCommand.APP_REQUEST, params, 100);

        ArgumentCaptor<EzyArray> dataCaptor = ArgumentCaptor.forClass(EzyArray.class);
        verify(handlerGroup, times(1)).fireDataReceived(
            dataCaptor.capture(),
            eq(100)
        );
        EzyArray data = dataCaptor.getValue();
        Asserts.assertEquals(data.size(), 2);
        Asserts.assertEquals(
            data.get(0, Integer.class),
            EzyCommand.APP_REQUEST.getId()
        );
        Asserts.assertEquals(data.get(1, EzyArray.class), params);
        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verifyNoMoreInteractions(handlerGroupManager);
    }

    @Test
    public void receiveCommandWithHandlerGroupNullTest() {
        EzyArray params = EzyEntityFactory.newArray();

        instance.receive(session, EzyCommand.APP_REQUEST, params, 100);

        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verifyNoMoreInteractions(handlerGroupManager);
        verifyZeroInteractions(handlerGroup);
    }

    @Test
    public void closeSessionTest() {
        when(handlerGroupManager.getHandlerGroup(connection))
            .thenReturn(handlerGroup);
        FieldUtil.setFieldValue(handlerGroup, "session", handlerGroupSession);

        instance.closeSession(session, EzyDisconnectReason.IDLE);

        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verify(handlerGroupSession, times(1)).disconnect(EzyDisconnectReason.IDLE);
        verifyNoMoreInteractions(handlerGroupManager);
    }

    @Test
    public void closeSessionWithSessionNullTest() {
        instance.closeSession(null, EzyDisconnectReason.IDLE);

        verifyZeroInteractions(handlerGroupManager);
    }

    @Test
    public void closeSessionWithHandlerGroupNullTest() {
        instance.closeSession(session, EzyDisconnectReason.IDLE);

        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verifyNoMoreInteractions(handlerGroupManager);
        verifyZeroInteractions(handlerGroup);
    }

    @Test
    public void exceptionCaughtTest() throws Exception {
        when(handlerGroupManager.getHandlerGroup(connection))
            .thenReturn(handlerGroup);
        EzyNioDataHandler dataHandler = mock(EzyNioDataHandler.class);
        FieldUtil.setFieldValue(handlerGroup, "handler", dataHandler);
        Exception exception = new Exception("test");

        instance.exceptionCaught(session, exception);

        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verify(dataHandler, times(1)).exceptionCaught(exception);
        verifyNoMoreInteractions(handlerGroupManager);
    }

    @Test
    public void exceptionCaughtWithSessionNullTest() {
        instance.exceptionCaught(null, new Exception("test"));

        verifyZeroInteractions(handlerGroupManager);
    }

    @Test
    public void exceptionCaughtWithHandlerGroupNullTest() {
        instance.exceptionCaught(session, new Exception("test"));

        verify(handlerGroupManager, times(1)).getHandlerGroup(connection);
        verifyNoMoreInteractions(handlerGroupManager);
        verifyZeroInteractions(handlerGroup);
    }
}
