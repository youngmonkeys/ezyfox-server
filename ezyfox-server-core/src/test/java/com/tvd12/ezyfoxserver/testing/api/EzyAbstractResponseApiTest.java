package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.api.EzyAbstractResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPool;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.reflect.MethodUtil;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class EzyAbstractResponseApiTest {

    @Test
    public void secureResponseNoRecipients() {
        // given
        EzyPackage packet = mock(EzyPackage.class);
        when(packet.getRecipients(EzyConnectionType.SOCKET)).thenReturn(Collections.emptyList());

        InternalResponseApi sut = new InternalResponseApi();

        // when
        MethodInvoker.create()
            .object(sut)
            .method("secureResponse")
            .param(EzyPackage.class, packet)
            .param(boolean.class, true)
            .invoke();

        // then
        verify(packet, times(1)).getRecipients(EzyConnectionType.SOCKET);
    }

    @Test
    public void dataToMessageContentTest() {
        // given
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;

        InternalResponseApi sut = new InternalResponseApi();

        // when
        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(sut)
                .method("dataToMessageContent")
                .param(EzyArray.class, data)
                .invoke()
        );

        // then
        Asserts.assertEquals(UnsupportedOperationException.class, e.getCause().getCause().getClass());
    }

    @Test
    public void encryptMessageContentTest() {
        // given
        byte[] messageContent = new byte[0];
        byte[] encryptionKey = new byte[0];

        InternalResponseApi sut = new InternalResponseApi();

        // when
        Throwable e = Asserts.assertThrows(() ->
            MethodUtil.invokeMethod("encryptMessageContent", sut, messageContent, encryptionKey));

        // then
        Asserts.assertEquals(UnsupportedOperationException.class, e.getCause().getCause().getClass());
    }

    @Test
    public void normalResponseActualTcp() throws Exception {
        // given
        InternalResponseApi sut = new InternalResponseApi();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(false);
        when(pack.getTransportType()).thenReturn(EzyTransportType.UDP_OR_TCP);

        EzySession session = mock(EzySession.class);

        doAnswer((it) -> {
            EzyPacket packet = it.getArgumentAt(0, EzyPacket.class);
            Asserts.assertEquals(packet.getTransportType(), EzyTransportType.TCP);
            return null;
        }).when(session).send(any(EzyPacket.class));

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, false);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verify(session, times(1)).send(any(EzyPacket.class));
        verify(session, times(1)).getDatagramChannelPool();
        verifyNoMoreInteractions(session);
    }

    @Test
    public void normalResponseActualUdp() throws Exception {
        // given
        InternalResponseApi sut = new InternalResponseApi();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(false);
        when(pack.getTransportType()).thenReturn(EzyTransportType.UDP_OR_TCP);

        EzySession session = mock(EzySession.class);
        EzyDatagramChannelPool datagramChannelPool = mock(EzyDatagramChannelPool.class);
        when(session.getDatagramChannelPool()).thenReturn(datagramChannelPool);

        doAnswer((it) -> {
            EzyPacket packet = it.getArgumentAt(0, EzyPacket.class);
            Asserts.assertEquals(packet.getTransportType(), EzyTransportType.UDP);
            return null;
        }).when(session).send(any(EzyPacket.class));

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, false);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verify(session, times(1)).send(any(EzyPacket.class));
        verify(session, times(1)).getDatagramChannelPool();
        verifyNoMoreInteractions(session);

        verifyNoMoreInteractions(datagramChannelPool);
    }

    @Test
    public void immediateResponseActualUdp() throws Exception {
        // given
        InternalResponseApi sut = new InternalResponseApi();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(false);
        when(pack.getTransportType()).thenReturn(EzyTransportType.UDP_OR_TCP);

        EzySession session = mock(EzySession.class);
        EzyDatagramChannelPool datagramChannelPool = mock(EzyDatagramChannelPool.class);
        when(session.getDatagramChannelPool()).thenReturn(datagramChannelPool);

        doAnswer((it) -> {
            EzyPacket packet = it.getArgumentAt(0, EzyPacket.class);
            Asserts.assertEquals(packet.getTransportType(), EzyTransportType.UDP);
            return null;
        }).when(session).sendNow(any(EzyPacket.class));

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, true);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verify(session, times(1)).sendNow(any(EzyPacket.class));
        verify(session, times(1)).getDatagramChannelPool();
        verifyNoMoreInteractions(session);

        verifyNoMoreInteractions(datagramChannelPool);
    }

    @Test
    public void normalResponseImmediateSendException() throws Exception {
        // given
        InternalResponseApi sut = new InternalResponseApi();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(false);

        EzySession session = mock(EzySession.class);
        RuntimeException error = new RuntimeException("test");
        doThrow(error).when(session).sendNow(any(EzyPacket.class));

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, true);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verify(session, times(1)).sendNow(any(EzyPacket.class));
        verifyNoMoreInteractions(session);
    }

    @Test
    public void normalResponseImmediateSendExceptionDueToCreatePacket() throws Exception {
        // given
        InternalResponseApi sut = new CreatePackFailedInternalResponseApi();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(false);

        EzySession session = mock(EzySession.class);

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, true);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verifyNoMoreInteractions(session);
    }

    @Test
    public void normalResponseSendException() throws Exception {
        // given
        InternalResponseApi sut = new InternalResponseApi();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(false);

        EzySession session = mock(EzySession.class);
        RuntimeException error = new RuntimeException("test");
        doThrow(error).when(session).send(any(EzyPacket.class));

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, false);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verify(session, times(1)).send(any(EzyPacket.class));
        verifyNoMoreInteractions(session);
    }

    @Test
    public void normalResponseSendExceptionDueToCreatePacket() throws Exception {
        // given
        InternalResponseApi sut = new CreatePackFailedInternalResponseApi();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(false);

        EzySession session = mock(EzySession.class);

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, false);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verifyNoMoreInteractions(session);
    }

    @Test
    public void secureResponseImmediateSendException() throws Exception {
        // given
        InternalResponseApi2 sut = new InternalResponseApi2();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(true);

        EzySession session = mock(EzySession.class);
        RuntimeException error = new RuntimeException("test");
        doThrow(error).when(session).sendNow(any(EzyPacket.class));

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, true);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verify(session, times(1)).sendNow(any(EzyPacket.class));
        verify(session, times(1)).getSessionKey();
        verifyNoMoreInteractions(session);
    }

    @Test
    public void secureResponseImmediateSendDueToCreatePacketException() throws Exception {
        // given
        InternalResponseApi2 sut = new CreatePackFailedInternalResponseApi2();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(true);

        EzySession session = mock(EzySession.class);

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, true);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verify(session, times(1)).getSessionKey();
        verifyNoMoreInteractions(session);
    }

    @Test
    public void secureResponseSendDueCreatePacketException() throws Exception {
        // given
        InternalResponseApi2 sut = new CreatePackFailedInternalResponseApi2();

        EzyPackage pack = mock(EzyPackage.class);
        when(pack.isEncrypted()).thenReturn(true);

        EzySession session = mock(EzySession.class);

        when(pack.getRecipients(EzyConnectionType.SOCKET)).thenReturn(
            Collections.singleton(session)
        );

        // when
        sut.response(pack, false);

        // then
        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verify(pack, times(1)).getData();
        verify(pack, times(1)).getTransportType();

        verifyNoMoreInteractions(pack);

        verify(session, times(1)).getSessionKey();
        verifyNoMoreInteractions(session);
    }

    private static class InternalResponseApi extends EzyAbstractResponseApi {

        @Override
        protected EzyConstant getConnectionType() {
            return EzyConnectionType.SOCKET;
        }

        @Override
        protected Object encodeData(EzyArray data) {
            return null;
        }
    }

    private static class InternalResponseApi2 extends EzyAbstractResponseApi {

        @Override
        protected EzyConstant getConnectionType() {
            return EzyConnectionType.SOCKET;
        }

        @Override
        protected Object encodeData(EzyArray data) {
            return null;
        }

        @Override
        protected byte[] dataToMessageContent(EzyArray data) {
            return new byte[0];
        }

        @Override
        protected byte[] encryptMessageContent(byte[] messageContent, byte[] encryptionKey) {
            return messageContent;
        }
    }

    private static class CreatePackFailedInternalResponseApi extends InternalResponseApi {

        @Override
        protected EzySimplePacket createPacket(EzyConstant transportType, Object bytes) {
            throw new RuntimeException("test");
        }
    }

    private static class CreatePackFailedInternalResponseApi2 extends InternalResponseApi2 {

        @Override
        protected EzySimplePacket createPacket(EzyConstant transportType, Object bytes) {
            throw new RuntimeException("test");
        }
    }
}
