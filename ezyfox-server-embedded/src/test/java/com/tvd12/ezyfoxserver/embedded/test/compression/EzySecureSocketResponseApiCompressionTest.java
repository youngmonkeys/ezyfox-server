package com.tvd12.ezyfoxserver.embedded.test.compression;

import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.codec.EzyMessageReaders;
import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfox.codec.MsgPackCodecCreator;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyEntityArrays;
import com.tvd12.ezyfoxserver.api.EzySecureSocketResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySecureChannel;
import com.tvd12.ezyfoxserver.socket.EzySimplePackage;
import com.tvd12.test.assertion.Asserts;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EzySecureSocketResponseApiCompressionTest {

    @Test
    public void secureChannelPacksCompressedBytesTest() throws Exception {
        // given
        EzyObjectToByteEncoder encoder = new MsgPackCodecCreator(false).newEncoder();
        EzySecureSocketResponseApi sut = new EzySecureSocketResponseApi(encoder, 512);

        CombineChannel channel = mock(CombineChannel.class);
        when(channel.getPackingLock()).thenReturn(new Object());
        when(channel.pack(any(byte[].class))).thenAnswer(it -> it.getArguments()[0]);

        EzySession session = mock(EzySession.class);
        when(session.getConnectionType()).thenReturn(EzyConnectionType.SOCKET.getName());
        when(session.isCompressionEnable()).thenReturn(true);
        when(session.getChannel()).thenReturn(channel);

        EzyArray data = EzyEntityArrays.newArray(1, newLongText());
        EzySimplePackage pack = new EzySimplePackage();
        pack.setData(data);
        pack.setEncrypted(false);
        pack.setTransportType(EzyTransportType.TCP);
        pack.addRecipient(session);

        // when
        sut.response(pack);

        // then
        ArgumentCaptor<byte[]> packedCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(channel, times(1)).pack(packedCaptor.capture());
        EzyMessage message = EzyMessageReaders.bytesToMessage(packedCaptor.getValue());
        Asserts.assertTrue(message.getHeader().isCompressed());
        Asserts.assertTrue(packedCaptor.getValue().length < newLongText().length());

        ArgumentCaptor<EzyPacket> packetCaptor = ArgumentCaptor.forClass(EzyPacket.class);
        verify(session, times(1)).send(packetCaptor.capture());
        EzyArray decoded = (EzyArray) new MsgPackCodecCreator(false)
            .newDecoder(Integer.MAX_VALUE)
            .decode(message, null);
        Asserts.assertEquals(decoded.get(1, String.class), data.get(1, String.class));
    }

    private static String newLongText() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 200; ++i) {
            builder.append("hello world ");
        }
        return builder.toString();
    }

    public interface CombineChannel
        extends EzyChannel, EzySecureChannel {}
}
