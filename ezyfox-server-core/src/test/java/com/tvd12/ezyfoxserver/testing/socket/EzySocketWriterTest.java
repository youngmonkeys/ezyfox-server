package com.tvd12.ezyfoxserver.testing.socket;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EzySocketWriterTest extends BaseTest {

    @Test
    public void test() throws Exception {
        EzySessionTicketsQueue sessionTicketsQueue = new EzyBlockingSessionTicketsQueue();

        EzySocketWriterGroupFetcher writerGroupFetcher = mock(EzySocketWriterGroupFetcher.class);
        EzySocketWriterGroup writerGroup = mock(EzySocketWriterGroup.class);
        when(writerGroupFetcher.getWriterGroup(any(EzySession.class))).thenReturn(writerGroup);

        EzySocketWriter socketWriter = new EzySocketWriter();
        socketWriter.setSessionTicketsQueue(sessionTicketsQueue);
        socketWriter.setWriterGroupFetcher(writerGroupFetcher);

        EzyPacketQueue packetQueue = new EzyNonBlockingPacketQueue();
        EzyPacket packet = new EzySimplePacket();
        packetQueue.add(packet);
        packetQueue.add(packet);

        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setActivated(true);
        session.setSessionTicketsQueue(sessionTicketsQueue);
        session.setPacketQueue(packetQueue);
        sessionTicketsQueue.add(session);

        socketWriter.handleEvent();
        packet.release();
        socketWriter.handleEvent();

        socketWriter.destroy();
    }

    @Test
    public void processSessionQueueEmptyCaseTest() {
        EzySessionTicketsQueue sessionTicketsQueue = new EzyBlockingSessionTicketsQueue();

        EzySocketWriterGroupFetcher writerGroupFetcher = mock(EzySocketWriterGroupFetcher.class);
        EzySocketWriterGroup writerGroup = mock(EzySocketWriterGroup.class);
        when(writerGroupFetcher.getWriterGroup(any(EzySession.class))).thenReturn(writerGroup);

        EzySocketWriter socketWriter = new EzySocketWriter();
        socketWriter.setSessionTicketsQueue(sessionTicketsQueue);
        socketWriter.setWriterGroupFetcher(writerGroupFetcher);

        EzyPacketQueue packetQueue = new EzyNonBlockingPacketQueue();

        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setActivated(true);
        session.setSessionTicketsQueue(sessionTicketsQueue);
        session.setPacketQueue(packetQueue);
        sessionTicketsQueue.add(session);

        socketWriter.handleEvent();
    }

    @Test
    public void packageQueueNullCaseTest() {
        EzySessionTicketsQueue sessionTicketsQueue = new EzyBlockingSessionTicketsQueue();

        EzySocketWriterGroupFetcher writerGroupFetcher = mock(EzySocketWriterGroupFetcher.class);
        EzySocketWriterGroup writerGroup = mock(EzySocketWriterGroup.class);
        when(writerGroupFetcher.getWriterGroup(any(EzySession.class))).thenReturn(writerGroup);

        EzySocketWriter socketWriter = new EzySocketWriter();
        socketWriter.setSessionTicketsQueue(sessionTicketsQueue);
        socketWriter.setWriterGroupFetcher(writerGroupFetcher);

        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setActivated(true);
        session.setSessionTicketsQueue(sessionTicketsQueue);
        session.setPacketQueue(null);
        sessionTicketsQueue.add(session);

        socketWriter.handleEvent();
    }

    @Test
    public void writerGroupNullCaseTest() {
        EzySessionTicketsQueue sessionTicketsQueue = new EzyBlockingSessionTicketsQueue();

        EzySocketWriterGroupFetcher writerGroupFetcher = mock(EzySocketWriterGroupFetcher.class);
        when(writerGroupFetcher.getWriterGroup(any(EzySession.class))).thenReturn(null);

        EzySocketWriter socketWriter = new EzySocketWriter();
        socketWriter.setSessionTicketsQueue(sessionTicketsQueue);
        socketWriter.setWriterGroupFetcher(writerGroupFetcher);

        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setActivated(true);
        session.setSessionTicketsQueue(sessionTicketsQueue);
        sessionTicketsQueue.add(session);

        socketWriter.handleEvent();
    }

    @Test
    public void processSessionTicketsQueue0ExceptionCase() {
        EzySessionTicketsQueue sessionTicketsQueue = new EzyBlockingSessionTicketsQueue();

        EzySocketWriterGroupFetcher writerGroupFetcher = mock(EzySocketWriterGroupFetcher.class);
        when(writerGroupFetcher.getWriterGroup(any(EzySession.class))).thenThrow(new IllegalArgumentException());

        EzySocketWriter socketWriter = new EzySocketWriter();
        socketWriter.setSessionTicketsQueue(sessionTicketsQueue);
        socketWriter.setWriterGroupFetcher(writerGroupFetcher);

        EzyPacketQueue packetQueue = new EzyNonBlockingPacketQueue();
        EzyPacket packet = new EzySimplePacket();
        packetQueue.add(packet);

        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setActivated(true);
        session.setSessionTicketsQueue(sessionTicketsQueue);
        session.setPacketQueue(packetQueue);
        sessionTicketsQueue.add(session);

        socketWriter.handleEvent();
    }

    @Test
    public void interruptedExceptionCaseTest() throws Exception {
        EzySessionTicketsQueue sessionTicketsQueue = new EzyBlockingSessionTicketsQueue();

        EzySocketWriterGroupFetcher writerGroupFetcher = mock(EzySocketWriterGroupFetcher.class);
        EzySocketWriterGroup writerGroup = mock(EzySocketWriterGroup.class);
        when(writerGroupFetcher.getWriterGroup(any(EzySession.class))).thenReturn(writerGroup);

        EzySocketWriter socketWriter = new EzySocketWriter();
        socketWriter.setSessionTicketsQueue(sessionTicketsQueue);
        socketWriter.setWriterGroupFetcher(writerGroupFetcher);

        Thread thread = new Thread(socketWriter::handleEvent);
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
    }
}
