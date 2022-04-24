package com.tvd12.ezyfoxserver.testing.entity;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyDroppedPackets;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDeliver;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.ezyfoxserver.statistics.EzyRequestFrameSecond;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSession;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class EzyAbstractSessionTest extends BaseCoreTest {

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void test() {
        MyTestSession session = new MyTestSession();
        session.setId(100);
        session.setPrivateKey(new byte[]{1, 2, 3});
        session.addReadBytes(10);
        session.addWrittenBytes(10);
        session.setLoggedInTime(12345);
        session.setMaxWaitingTime(123);
        session.setMaxIdleTime(12345L);
        assertEquals(session.getPrivateKey(), new byte[]{1, 2, 3});
        assertEquals(session.getReadBytes(), 10);
        assertEquals(session.getWrittenBytes(), 10);
        assertEquals(session.getLoggedInTime(), 12345L);
        assertEquals(session.getMaxWaitingTime(), 123L);
        MyTestSession session2 = new MyTestSession();
        session2.setId(1);

        MyTestSession session3 = new MyTestSession();
        session3.setId(100);

        assert !session.equals(null);
        assert session.equals(session);
        assert !session.equals(new Object());
        assert !session.equals(session2);
        assert !session.equals(new PrivateSession());
        assert session.equals(session3);
        assert session.hashCode() != session2.hashCode();

        session.setClientId("clientId");
        assert session.getClientId().equals("clientId");
        session.setOwnerName("owner");
        assert session.getOwnerName().equals("owner");
        session.setLastReadTime(1);
        assert session.getLastReadTime() == 1;
        session.setLastWriteTime(2);
        assert session.getLastWriteTime() == 2;
        session.setLastActivityTime(3);
        assert session.getLastActivityTime() == 3;
        session.setReadRequests(4);
        assert session.getReadRequests() == 4;
        session.setWrittenResponses(5);
        assert session.getWrittenResponses() == 5;
        session.addWrittenResponses(1);
        assert session.getWrittenResponses() == 6;
        session.setDestroyed(false);
        assert !session.isDestroyed();
        session.setStreamingEnable(true);
        assert session.isStreamingEnable();
        session.setClientType("android");
        assert session.getClientType().equals("android");
        session.setClientVersion("1.0.0");
        assert session.getClientVersion().equals("1.0.0");
        session.setBeforeToken("before");
        assert session.getBeforeToken().equals("before");
        session.setDisconnectReason(EzyDisconnectReason.ADMIN_BAN);
        assert session.getDisconnectReason() == EzyDisconnectReason.ADMIN_BAN;
        session.setMaxIdleTime(6);
        assert session.getMaxIdleTime() == 6;
        session.setDroppedPackets(mock(EzyDroppedPackets.class));
        assert session.getDroppedPackets() != null;
        session.setImmediateDeliver(mock(EzyImmediateDeliver.class));
        assert session.getImmediateDeliver() != null;

        EzySessionTicketsQueue sessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
        session.setSessionTicketsQueue(sessionTicketsQueue);
        assert session.getSessionTicketsQueue() == sessionTicketsQueue;

        EzySocketDisconnectionQueue disconnectionQueue = mock(EzySocketDisconnectionQueue.class);
        session.setDisconnectionQueue(disconnectionQueue);
        assert session.getDisconnectionQueue() == disconnectionQueue;
        assert !session.isDisconnectionRegistered();
        assert session.getDisconnectionLock() != null;
        assert session.getLocks().isEmpty();
        assert session.getLock("test") != null;

        assert !session.isActivated();
        session.send(mock(EzyPacket.class));
        session.setActivated(true);
        EzyPacketQueue packetQueue = new EzyNonBlockingPacketQueue(3);
        session.setPacketQueue(packetQueue);
        session.send(mock(EzyPacket.class));
        assert session.getPacketQueue() != null;
        session.send(mock(EzyPacket.class));
        session.sendNow(mock(EzyPacket.class));
        session.send(mock(EzyPacket.class));
        session.send(mock(EzyPacket.class));
        session.send(mock(EzyPacket.class));

        assert session.getChannel() == null;
        assert session.getConnection() == null;

        session.disconnect();
        session.close();
        session.destroy();
        session.destroy();
        assert session.isDestroyed();

        PrivateSession privateSession = new PrivateSession();
        privateSession.setDisconnectionQueue(disconnectionQueue);
        privateSession.disconnect();
        privateSession.disconnect();
        assert privateSession.getServerAddress() == null;
        EzyChannel channel = mock(EzyChannel.class);
        when(channel.getServerAddress()).thenReturn(new InetSocketAddress(2233));
        privateSession.setChannel(channel);
        assert privateSession.getServerAddress() != null;
        privateSession.close();
        assert privateSession.getConnection() == null;
        assert privateSession.getUdpClientAddress() == null;
        assert privateSession.getDatagramChannel() == null;
        assert privateSession.getDatagramChannelPool() == null;
        privateSession.setUdpClientAddress(new InetSocketAddress(12345));
        assert privateSession.getUdpClientAddress() != null;
        try {
            privateSession.setDatagramChannel(DatagramChannel.open());
            assert privateSession.getDatagramChannel() != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            privateSession.setDatagramChannelPool(new EzyDatagramChannelPool(2));
            assert privateSession.getDatagramChannelPool() != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        privateSession.setRequestFrameInSecond(new EzyRequestFrameSecond(2));
        Asserts.assertFalse(privateSession.addReceviedRequests(1));
        Asserts.assertTrue(privateSession.addReceviedRequests(3));

        privateSession.setRequestFrameInSecond(new EzyRequestFrameSecond(5, System.currentTimeMillis() - 2 * 1000));
        Asserts.assertFalse(privateSession.addReceviedRequests(1));
        Asserts.assertFalse(privateSession.addReceviedRequests(3));
        Asserts.assertFalse(privateSession.getRequestFrameInSecond().isExpired());

        byte[] clientKey = RandomUtil.randomShortByteArray();
        privateSession.setClientKey(clientKey);
        Asserts.assertEquals(clientKey, privateSession.getClientKey());

        byte[] sessionKey = RandomUtil.randomShortByteArray();
        privateSession.setSessionKey(sessionKey);
        Asserts.assertEquals(sessionKey, privateSession.getSessionKey());

        EzyRequestQueue systemRquestQueue = mock(EzyRequestQueue.class);
        privateSession.setSystemRequestQueue(systemRquestQueue);
        Asserts.assertEquals(systemRquestQueue, privateSession.getSystemRequestQueue());

        EzyRequestQueue extensionRequestQueue = mock(EzyRequestQueue.class);
        privateSession.setExtensionRequestQueue(extensionRequestQueue);
        Asserts.assertEquals(extensionRequestQueue, privateSession.getExtensionRequestQueue());
        privateSession.destroy();
    }

    @Test
    public void addPacketToSessionQueueWithTicketsQueueIsNull() {
        // given
        EzyPacket packet = mock(EzyPacket.class);
        MyTestSession session = new MyTestSession();
        session.setActivated(true);

        EzyPacketQueue packetQueue = mock(EzyPacketQueue.class);
        when(packetQueue.isEmpty()).thenReturn(true);
        when(packetQueue.add(packet)).thenReturn(true);
        session.setPacketQueue(packetQueue);

        // when
        session.send(packet);

        // then
        Asserts.assertNull(session.getSessionTicketsQueue());
    }

    @Test
    public void addPacketToSessionQueueWithDroppedPacketsNowIsNull() {
        // given
        EzyPacket packet = mock(EzyPacket.class);
        MyTestSession session = new MyTestSession();
        session.setActivated(true);

        EzyPacketQueue packetQueue = mock(EzyPacketQueue.class);
        when(packetQueue.isEmpty()).thenReturn(true);
        when(packetQueue.add(packet)).thenReturn(false);
        session.setPacketQueue(packetQueue);

        // when
        session.send(packet);

        // then
        Asserts.assertNull(session.getDroppedPackets());
    }

    @Test
    public void disconnectWithQueueIsNull() {
        // given
        PrivateSession session = new PrivateSession();

        // when
        session.disconnect(EzyDisconnectReason.ADMIN_BAN);

        // then
        Asserts.assertNull(session.getDisconnectionQueue());
        Asserts.assertTrue(session.isDisconnectionRegistered());
    }

    private static class PrivateSession extends EzyAbstractSession {
        private static final long serialVersionUID = -3656335144134244222L;
    }
}
