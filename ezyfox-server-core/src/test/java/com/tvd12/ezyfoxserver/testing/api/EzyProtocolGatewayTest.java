package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolChannel;
import com.tvd12.ezyfoxserver.protocol.EzyProtocolGateway;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyProtocolGatewayTest extends BaseTest {

    @Test
    public void protocolChannelIsBinaryByDefaultTest() {
        EzyProtocolChannel channel = new TestProtocolChannel();

        assert channel.isBinary();
    }

    @Test
    public void closeSessionUsesUnknownReasonByDefaultTest() {
        TestProtocolGateway gateway = new TestProtocolGateway();
        EzySession session = null;

        gateway.closeSession(session);

        assert gateway.closedSession == session;
        assert gateway.disconnectReason == EzyDisconnectReason.UNKNOWN;
    }

    private static class TestProtocolGateway implements EzyProtocolGateway {

        private EzySession closedSession;
        private EzyConstant disconnectReason;

        @Override
        public EzySession openSession(EzyProtocolChannel channel) {
            return null;
        }

        @Override
        public EzySession getSession(Object connection) {
            return null;
        }

        @Override
        public void receive(EzySession session, EzyArray data, int dataSize) {
            // Not used by this test.
        }

        @Override
        public void receive(
            EzySession session,
            EzyCommand command,
            EzyArray params,
            int dataSize
        ) {
            // Not used by this test.
        }

        @Override
        public void closeSession(EzySession session, EzyConstant reason) {
            this.closedSession = session;
            this.disconnectReason = reason;
        }

        @Override
        public void exceptionCaught(EzySession session, Throwable throwable) {
            // Not used by this test.
        }
    }

    private static class TestProtocolChannel implements EzyProtocolChannel {

        @Override
        public Object encode(EzyArray data) {
            return null;
        }

        @Override
        public void close() {
            // Not used by this test.
        }

        @Override
        public void disconnect() {
            // Not used by this test.
        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public int write(Object data, boolean binary) {
            return 0;
        }

        @Override
        public <T> T getConnection() {
            return null;
        }

        @Override
        public String getConnectionType() {
            return null;
        }

        @Override
        public java.net.SocketAddress getServerAddress() {
            return null;
        }

        @Override
        public java.net.SocketAddress getClientAddress() {
            return null;
        }
    }
}
