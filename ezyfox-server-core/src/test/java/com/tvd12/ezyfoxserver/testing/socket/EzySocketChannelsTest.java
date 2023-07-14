package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketChannels;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import javax.net.ssl.SSLException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class EzySocketChannelsTest {

    @Test
    public void writeTimeOut() {
        // given
        long timeoutAt = System.currentTimeMillis() - 1000;
        SocketChannel socketChannel = mock(SocketChannel.class);
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put(new byte[] {1, 2});

        // when
        Throwable e = Asserts.assertThrows(() ->
            EzySocketChannels.write(
                socketChannel,
                buffer,
                timeoutAt
            )
        );

        // then
        Asserts.assertEqualsType(e, SSLException.class);

        verifyNoMoreInteractions(socketChannel);
    }
}
