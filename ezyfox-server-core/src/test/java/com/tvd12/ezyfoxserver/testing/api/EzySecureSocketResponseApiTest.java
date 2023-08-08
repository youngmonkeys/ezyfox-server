package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfoxserver.api.EzySecureSocketResponseApi;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySecureChannel;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class EzySecureSocketResponseApiTest {

    private EzySession session;
    private CombineChannel channel;
    private EzyPacket packet;
    private EzyObjectToByteEncoder encoder;
    private final byte[] data = new byte[0];
    private final byte[] packedData = new byte[0];
    private final Object packLock = new Object();
    private EzySecureSocketResponseApi instance;

    @BeforeMethod
    public void setup() {
        session = mock(EzySession.class);
        channel = mock(CombineChannel.class);
        packet = mock(EzyPacket.class);
        encoder = mock(EzyObjectToByteEncoder.class);
        instance = new EzySecureSocketResponseApi(encoder);

        when(session.getChannel()).thenReturn(channel);
        when(channel.getPackLock()).thenReturn(packLock);
        when(packet.getData()).thenReturn(data);
    }

    @AfterMethod
    private void verifyAll() {
        verifyNoMoreInteractions(encoder);

        verify(session, times(1)).getChannel();
        verifyNoMoreInteractions(session);

        verifyNoMoreInteractions(channel);
        verifyNoMoreInteractions(packet);
    }

    @Test
    public void sendTcpPacketNormalCase() throws Exception {
        // given
        when(channel.pack(data)).thenReturn(packedData);

        MethodInvoker.create()
            .object(instance)
            .method("sendTcpPacket")
            .param(EzySession.class, session)
            .param(EzyPacket.class, packet)
            .invoke();

        // then
        verify(channel, times(1)).pack(data);
        verify(channel, times(1)).getPackLock();

        verify(packet, times(1)).getData();
        verify(packet, times(1)).replaceData(packedData);

        verify(session, times(1)).send(packet);
    }

    @Test
    public void sendTcpPacketThrowCloseExceptionCase() throws Exception {
        // given
        EzyConnectionCloseException exception =
            new EzyConnectionCloseException("test");
        when(channel.pack(data)).thenThrow(exception);

        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(instance)
                .method("sendTcpPacket")
                .param(EzySession.class, session)
                .param(EzyPacket.class, packet)
                .invoke()
        );

        // then
        Asserts.assertEqualsType(e.getCause().getCause(), EzyConnectionCloseException.class);

        verify(channel, times(1)).pack(data);
        verify(channel, times(1)).getPackLock();

        verify(packet, times(1)).getData();

        verify(session, times(1)).disconnect();
    }

    @Test
    public void sendTcpPacketSessionDisconnectedExceptionCase() {
        // given
        when(session.getChannel()).thenReturn(null);

        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(instance)
                .method("sendTcpPacket")
                .param(EzySession.class, session)
                .param(EzyPacket.class, packet)
                .invoke()
        );

        // then
        Asserts.assertEqualsType(e.getCause().getCause(), IOException.class);
    }

    @Test
    public void sendTcpPacketNowNormalCase() throws Exception {
        // given
        when(channel.pack(data)).thenReturn(packedData);

        MethodInvoker.create()
            .object(instance)
            .method("sendTcpPacketNow")
            .param(EzySession.class, session)
            .param(EzyPacket.class, packet)
            .invoke();

        // then
        verify(channel, times(1)).pack(data);
        verify(channel, times(1)).getPackLock();

        verify(packet, times(1)).getData();
        verify(packet, times(1)).replaceData(packedData);

        verify(session, times(1)).sendNow(packet);
    }

    @Test
    public void sendTcpPacketNowThrowCloseExceptionCase() throws Exception {
        // given
        EzyConnectionCloseException exception =
            new EzyConnectionCloseException("test");
        when(channel.pack(data)).thenThrow(exception);

        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(instance)
                .method("sendTcpPacketNow")
                .param(EzySession.class, session)
                .param(EzyPacket.class, packet)
                .invoke()
        );

        // then
        Asserts.assertEqualsType(e.getCause().getCause(), EzyConnectionCloseException.class);

        verify(channel, times(1)).pack(data);
        verify(channel, times(1)).getPackLock();

        verify(packet, times(1)).getData();

        verify(session, times(1)).disconnect();
    }

    @Test
    public void sendTcpPacketNowSessionDisconnectedExceptionCase() {
        // given
        when(session.getChannel()).thenReturn(null);

        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(instance)
                .method("sendTcpPacketNow")
                .param(EzySession.class, session)
                .param(EzyPacket.class, packet)
                .invoke()
        );

        // then
        Asserts.assertEqualsType(e.getCause().getCause(), IOException.class);
    }

    public interface CombineChannel
        extends EzyChannel, EzySecureChannel {}
}
