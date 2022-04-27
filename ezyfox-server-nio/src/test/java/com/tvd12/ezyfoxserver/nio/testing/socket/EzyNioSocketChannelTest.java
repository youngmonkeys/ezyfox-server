package com.tvd12.ezyfoxserver.nio.testing.socket;

import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketChannel;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

import static org.mockito.Mockito.spy;

public class EzyNioSocketChannelTest extends BaseTest {

    @Test
    public void test() throws Exception {
        ExSocketChannel socketChannel = spy(ExSocketChannel.class);
        EzyNioSocketChannel channel = new EzyNioSocketChannel(socketChannel);
        System.out.println(channel.getChannel());
        System.out.println(channel.getServerAddress());
        System.out.println(channel.isConnected());
        channel.disconnect();
        channel.close();
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put("hello".getBytes());
        buffer.flip();
        channel.write(buffer, true);
    }

    public static abstract class ExSocketChannel extends SocketChannel {

        public ExSocketChannel() {
            super(SelectorProvider.provider());
        }

    }
}
