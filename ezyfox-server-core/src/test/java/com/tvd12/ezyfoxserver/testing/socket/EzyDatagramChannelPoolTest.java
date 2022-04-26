package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPool;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class EzyDatagramChannelPoolTest {

    @Test
    public void test() {
        EzyDatagramChannelPool pool = new EzyDatagramChannelPool(16);
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9005);
        try {
            pool.bind(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Selector selector = Selector.open();
            pool.register(selector);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert pool.getChannel() != null;
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testNewChannelException() {
        new EzyDatagramChannelPool(16) {
            @Override
            protected DatagramChannel openChannel() throws IOException {
                throw new IOException("test");
            }
        };
    }

    @Test
    public void testBindRegisterCloseChannelException() {
        EzyDatagramChannelPool pool = new EzyDatagramChannelPool(2) {
            @Override
            protected DatagramChannel openChannel() throws IOException {
                MyDatagramChannel1 channel = spy(MyDatagramChannel1.class);
                DatagramSocket socket = new DatagramSocket();
                when(channel.socket()).thenReturn(socket);
                return channel;
            }
        };
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9005);
        try {
            pool.bind(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Selector selector = Selector.open();
            pool.register(selector);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static abstract class MyDatagramChannel1 extends DatagramChannel {

        protected MyDatagramChannel1() {
            super(SelectorProvider.provider());
        }

        @Override
        public DatagramChannel bind(SocketAddress local) throws IOException {
            throw new IOException("bind test");
        }

        @Override
        protected void implCloseSelectableChannel() throws IOException {
            throw new IOException("close test");
        }
    }
}
