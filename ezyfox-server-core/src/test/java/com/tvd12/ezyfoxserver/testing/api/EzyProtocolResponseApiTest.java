package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.api.EzyProtocolResponseApi;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolChannel;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class EzyProtocolResponseApiTest extends BaseTest {

    private static final String CONNECTION_TYPE = "protocol";
    private static final Object BYTES = new byte[] {1, 2, 3};

    @Test
    public void responseSendsPacketImmediatelyTest() throws Exception {
        EzyArray data = mock(EzyArray.class);
        EzySession session = mock(EzySession.class);
        EzyProtocolChannel channel = mock(EzyProtocolChannel.class);
        EzyPackage pack = packageWith(data, session);
        when(session.getChannel()).thenReturn(channel);
        when(channel.encode(data)).thenReturn(BYTES);
        when(channel.isBinary()).thenReturn(false);

        new EzyProtocolResponseApi().response(pack, CONNECTION_TYPE, true);

        verify(channel).encode(data);
        ArgumentCaptor<EzyPacket> packetCaptor = ArgumentCaptor.forClass(EzyPacket.class);
        verify(session).sendNow(packetCaptor.capture());
        EzySimplePacket packet = (EzySimplePacket) packetCaptor.getValue();
        assert packet.getData() == BYTES;
        assert !packet.isBinary();
        verify(session, never()).send(any(EzyPacket.class));
    }

    @Test
    public void responseSendsPacketNormallyTest() throws Exception {
        EzyArray data = mock(EzyArray.class);
        EzySession session = mock(EzySession.class);
        EzyProtocolChannel channel = mock(EzyProtocolChannel.class);
        EzyPackage pack = packageWith(data, session);
        when(session.getChannel()).thenReturn(channel);
        when(channel.encode(data)).thenReturn(BYTES);
        when(channel.isBinary()).thenReturn(true);

        new EzyProtocolResponseApi().response(pack, CONNECTION_TYPE, false);

        verify(channel).encode(data);
        ArgumentCaptor<EzyPacket> packetCaptor = ArgumentCaptor.forClass(EzyPacket.class);
        verify(session).send(packetCaptor.capture());
        EzySimplePacket packet = (EzySimplePacket) packetCaptor.getValue();
        assert packet.getData() == BYTES;
        assert packet.isBinary();
        verify(session, never()).sendNow(any(EzyPacket.class));
    }

    @Test
    public void responseSkipsNonProtocolChannelTest() {
        EzyArray data = mock(EzyArray.class);
        EzySession session = mock(EzySession.class);
        EzyChannel channel = mock(EzyChannel.class);
        EzyPackage pack = packageWith(data, session);
        when(session.getChannel()).thenReturn(channel);

        new EzyProtocolResponseApi().response(pack, CONNECTION_TYPE, false);

        verify(session).getChannel();
        verifyNoMoreInteractions(session);
        verifyZeroInteractions(channel);
    }

    @Test
    public void responseSkipsNullEncodedBytesTest() throws Exception {
        EzyArray data = mock(EzyArray.class);
        EzySession session = mock(EzySession.class);
        EzyProtocolChannel channel = mock(EzyProtocolChannel.class);
        EzyPackage pack = packageWith(data, session);
        when(session.getChannel()).thenReturn(channel);
        when(channel.encode(data)).thenReturn(null);

        new EzyProtocolResponseApi().response(pack, CONNECTION_TYPE, false);

        verify(channel).encode(data);
        verify(session).getChannel();
        verify(session, never()).send(any(EzyPacket.class));
        verify(session, never()).sendNow(any(EzyPacket.class));
    }

    @Test
    public void responseReleasesPacketWhenSendingFailsTest() throws Exception {
        EzyArray data = mock(EzyArray.class);
        EzySession session = mock(EzySession.class);
        EzyProtocolChannel channel = mock(EzyProtocolChannel.class);
        EzyPackage pack = packageWith(data, session);
        when(session.getChannel()).thenReturn(channel);
        when(channel.encode(data)).thenReturn(BYTES);
        doThrow(new IllegalStateException("send failed"))
            .when(session)
            .send(any(EzyPacket.class));

        new EzyProtocolResponseApi().response(pack, CONNECTION_TYPE, false);

        ArgumentCaptor<EzyPacket> packetCaptor = ArgumentCaptor.forClass(EzyPacket.class);
        verify(session).send(packetCaptor.capture());
        assert ((EzySimplePacket) packetCaptor.getValue()).isReleased();
    }

    @Test
    public void responseHandlesChannelFailureAndContinuesToNextSessionTest() throws Exception {
        EzyArray data = mock(EzyArray.class);
        EzySession failedSession = mock(EzySession.class);
        EzySession successfulSession = mock(EzySession.class);
        EzyProtocolChannel failedChannel = mock(EzyProtocolChannel.class);
        EzyProtocolChannel successfulChannel = mock(EzyProtocolChannel.class);
        EzyPackage pack = mock(EzyPackage.class);
        when(pack.getData()).thenReturn(data);
        when(pack.getRecipients(CONNECTION_TYPE))
            .thenReturn(java.util.Arrays.asList(failedSession, successfulSession));
        when(failedSession.getChannel()).thenReturn(failedChannel);
        when(failedChannel.encode(data)).thenThrow(new IllegalStateException("encode failed"));
        when(successfulSession.getChannel()).thenReturn(successfulChannel);
        when(successfulChannel.encode(data)).thenReturn(BYTES);

        new EzyProtocolResponseApi().response(pack, CONNECTION_TYPE, false);

        verify(failedSession, never()).send(any(EzyPacket.class));
        verify(successfulSession).send(any(EzyPacket.class));
    }

    private EzyPackage packageWith(EzyArray data, EzySession session) {
        EzyPackage pack = mock(EzyPackage.class);
        when(pack.getData()).thenReturn(data);
        when(pack.getRecipients(CONNECTION_TYPE))
            .thenReturn(Collections.singletonList(session));
        return pack;
    }
}
