package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.net.SocketAddress;

public class EzyChannelTest {

    @Test
    public void pack() throws Exception {
        // given
        EzyChannel channel = new TestEzyChannel();
        byte[] bytes = RandomUtil.randomShortByteArray();

        // when
        byte[] actual = channel.pack(bytes);

        // then
        Asserts.assertEquals(actual, bytes);
    }

    public static class TestEzyChannel implements EzyChannel {

        @Override
        public void close() {
        }

        @Override
        public void disconnect() {
        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public int write(Object data, boolean binary) throws Exception {
            return 0;
        }

        @Override
        public <T> T getConnection() {
            return null;
        }

        @Override
        public EzyConnectionType getConnectionType() {
            return null;
        }

        @Override
        public SocketAddress getServerAddress() {
            return null;
        }

        @Override
        public SocketAddress getClientAddress() {
            return null;
        }
    }
}
