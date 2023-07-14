package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.api.EzySocketResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySimplePackage;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EzySocketResponseApiTest {

    @Test
    public void normalResponseTest() throws Exception {
        // given
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
        EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
        byte[] bytes = RandomUtil.randomShortByteArray();
        when(encoder.encode(data)).thenReturn(bytes);
        EzySocketResponseApi sut = new EzySocketResponseApi(encoder);

        EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(false);
        pack.setTransportType(EzyTransportType.TCP);

        int sessionCount = RandomUtil.randomSmallInt() + 1;
        List<EzySession> sessions = RandomUtil.randomList(sessionCount, () -> {
            EzySession session = mock(EzySession.class);
            when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
            return session;
        });
        pack.addRecipients(sessions);


        // when
        sut.response(pack);

        // then
        verify(encoder, times(1)).encode(data);
        verify(encoder, times(0)).toMessageContent(data);
        verify(encoder, times(0)).encryptMessageContent(any(byte[].class), any(byte[].class));
    }


    @Test
    public void secureResponseTest() throws Exception {
        // given
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
        byte[] bytes = RandomUtil.randomShortByteArray();
        EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
        when(encoder.toMessageContent(data)).thenReturn(bytes);
        EzySocketResponseApi sut = new EzySocketResponseApi(encoder);

        EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(true);
        pack.setTransportType(EzyTransportType.TCP);

        int sessionCount = RandomUtil.randomSmallInt() + 1;
        List<EzySession> sessions = RandomUtil.randomList(sessionCount, () -> {
            EzySession session = mock(EzySession.class);
            when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
            return session;
        });
        pack.addRecipients(sessions);


        // when
        sut.response(pack);

        // then
        verify(encoder, times(1)).toMessageContent(data);
        verify(encoder, times(sessionCount)).encryptMessageContent(any(byte[].class), any(byte[].class));
    }

    @Test
    public void secureResponseImmediateTest() throws Exception {
        // given
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
        byte[] bytes = RandomUtil.randomShortByteArray();
        EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
        when(encoder.toMessageContent(data)).thenReturn(bytes);
        EzySocketResponseApi sut = new EzySocketResponseApi(encoder);

        EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(true);
        pack.setTransportType(EzyTransportType.TCP);

        int sessionCount = RandomUtil.randomSmallInt() + 1;
        List<EzySession> sessions = RandomUtil.randomList(sessionCount, () -> {
            EzySession session = mock(EzySession.class);
            when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET);
            return session;
        });
        pack.addRecipients(sessions);


        // when
        sut.response(pack, true);

        // then
        verify(encoder, times(1)).toMessageContent(data);
        verify(encoder, times(sessionCount)).encryptMessageContent(any(byte[].class), any(byte[].class));
    }

    @Test
    public void packMessage() throws Exception {
        // given
        EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
        EzySocketResponseApi instance = new EzySocketResponseApi(encoder);

        byte[] message = RandomUtil.randomShortByteArray();
        EzyChannel channel = mock(EzyChannel.class);
        when(channel.pack(message)).thenReturn(message);

        EzySession session = mock(EzySession.class);
        when(session.getChannel()).thenReturn(channel);

        // when
        Object actual = MethodInvoker.create()
            .object(instance)
            .method("packMessage")
            .param(EzySession.class, session)
            .param(Object.class, message)
            .invoke();

        // then
        Asserts.assertEquals(actual, message);

        verify(session, times(1)).getChannel();
        verifyNoMoreInteractions(session);

        verify(channel, times(1)).pack(message);
        verifyNoMoreInteractions(channel);
    }

    @Test
    public void packMessageThrowsException() throws Exception {
        // given
        EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
        EzySocketResponseApi instance = new EzySocketResponseApi(encoder);

        byte[] message = RandomUtil.randomShortByteArray();
        EzyChannel channel = mock(EzyChannel.class);
        EzyConnectionCloseException error = new EzyConnectionCloseException("test");
        when(channel.pack(message)).thenThrow(error);

        EzySession session = mock(EzySession.class);
        when(session.getChannel()).thenReturn(channel);

        // when
        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(instance)
                .method("packMessage")
                .param(EzySession.class, session)
                .param(Object.class, message)
                .invoke()
        );

        // then
        Asserts.assertEqualsType(
            e.getCause().getCause(),
            EzyConnectionCloseException.class
        );

        verify(session, times(1)).getChannel();
        verify(session, times(1)).disconnect();
        verifyNoMoreInteractions(session);

        verify(channel, times(1)).pack(message);
        verifyNoMoreInteractions(channel);
    }
}
