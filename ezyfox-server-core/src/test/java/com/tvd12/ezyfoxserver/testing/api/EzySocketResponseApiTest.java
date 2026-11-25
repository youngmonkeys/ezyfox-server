package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.api.EzySocketResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySimplePackage;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.mockito.ArgumentCaptor;
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
            when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
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
            when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
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
            when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
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
    public void normalResponseWithCompressionTest() throws Exception {
        // given
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
        EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
        byte[] bytes = RandomUtil.randomShortByteArray();
        byte[] messageContent = new byte[1024];
        byte[] compressedMessageContent = new byte[16];
        byte[] compressedBytes = RandomUtil.randomShortByteArray();
        when(encoder.encode(data)).thenReturn(bytes);
        when(encoder.toMessageContent(data)).thenReturn(messageContent);
        when(encoder.compressMessageContent(messageContent))
            .thenReturn(compressedMessageContent);
        when(encoder.encryptMessageContent(compressedMessageContent, null, true))
            .thenReturn(compressedBytes);
        EzySocketResponseApi sut = new EzySocketResponseApi(encoder, 512);

        EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(false);
        pack.setTransportType(EzyTransportType.TCP);

        EzySession compressionSession = mock(EzySession.class);
        when(compressionSession.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
        when(compressionSession.isCompressionEnable()).thenReturn(true);
        EzySession compressionSession2 = mock(EzySession.class);
        when(compressionSession2.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
        when(compressionSession2.isCompressionEnable()).thenReturn(true);
        EzySession normalSession = mock(EzySession.class);
        when(normalSession.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
        pack.addRecipient(compressionSession);
        pack.addRecipient(compressionSession2);
        pack.addRecipient(normalSession);

        // when
        sut.response(pack);

        // then
        verify(encoder, times(1)).encode(data);
        verify(encoder, times(1)).toMessageContent(data);
        verify(encoder, times(1)).compressMessageContent(messageContent);
        verify(encoder, times(1)).encryptMessageContent(compressedMessageContent, null, true);
        Asserts.assertEquals(sentData(compressionSession), compressedBytes);
        Asserts.assertEquals(sentData(compressionSession2), compressedBytes);
        Asserts.assertEquals(sentData(normalSession), bytes);
    }

    @Test
    public void normalResponseWithCompressionUnderThresholdTest() throws Exception {
        // given
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
        EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
        byte[] messageContent = new byte[100];
        byte[] bytes = RandomUtil.randomShortByteArray();
        when(encoder.toMessageContent(data)).thenReturn(messageContent);
        when(encoder.encryptMessageContent(messageContent, null)).thenReturn(bytes);
        EzySocketResponseApi sut = new EzySocketResponseApi(encoder, 512);

        EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(false);
        pack.setTransportType(EzyTransportType.TCP);

        EzySession session = mock(EzySession.class);
        when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
        when(session.isCompressionEnable()).thenReturn(true);
        pack.addRecipient(session);

        // when
        sut.response(pack);

        // then
        verify(encoder, times(0)).encode(data);
        verify(encoder, times(0)).compressMessageContent(any(byte[].class));
        verify(encoder, times(1)).encryptMessageContent(messageContent, null);
        Asserts.assertEquals(sentData(session), bytes);
    }

    @Test
    public void secureResponseWithCompressionTest() throws Exception {
        // given
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;
        byte[] messageContent = new byte[1024];
        byte[] compressedMessageContent = new byte[16];
        EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
        when(encoder.toMessageContent(data)).thenReturn(messageContent);
        when(encoder.compressMessageContent(messageContent))
            .thenReturn(compressedMessageContent);
        EzySocketResponseApi sut = new EzySocketResponseApi(encoder, 512);

        EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(true);
        pack.setTransportType(EzyTransportType.TCP);

        byte[] sessionKey = RandomUtil.randomShortByteArray();
        EzySession compressionSession = mock(EzySession.class);
        when(compressionSession.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
        when(compressionSession.isCompressionEnable()).thenReturn(true);
        when(compressionSession.getSessionKey()).thenReturn(sessionKey);
        EzySession normalSession = mock(EzySession.class);
        when(normalSession.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
        when(normalSession.getSessionKey()).thenReturn(sessionKey);
        pack.addRecipient(compressionSession);
        pack.addRecipient(normalSession);

        // when
        sut.response(pack, true);

        // then
        verify(encoder, times(1)).toMessageContent(data);
        verify(encoder, times(1)).compressMessageContent(messageContent);
        verify(encoder, times(1)).encryptMessageContent(compressedMessageContent, sessionKey, true);
        verify(encoder, times(1)).encryptMessageContent(messageContent, sessionKey);
    }

    private static Object sentData(EzySession session) throws Exception {
        ArgumentCaptor<EzyPacket> captor = ArgumentCaptor.forClass(EzyPacket.class);
        verify(session, times(1)).send(captor.capture());
        return captor.getValue().getData();
    }
}
