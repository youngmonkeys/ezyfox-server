package com.tvd12.ezyfoxserver.nio.udp;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

import com.tvd12.ezyfoxserver.nio.handler.EzyNioUdpDataHandler;
import com.tvd12.ezyfoxserver.socket.EzySimpleUdpReceivedPacket;
import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;
import com.tvd12.ezyfoxserver.socket.EzyUdpReceivedPacket;

import lombok.Setter;

public class EzyNioUdpReader extends EzySocketAbstractEventHandler {

    protected final ByteBuffer buffer;
    @Setter
    protected Selector ownSelector;
    @Setter
    protected EzyNioUdpDataHandler udpDataHandler;

    public EzyNioUdpReader(int maxBufferSize) {
        this.buffer = ByteBuffer.allocateDirect(maxBufferSize);
    }

    @Override
    public void destroy() {
        processWithLogException(() -> ownSelector.close());
    }

    @Override
    public void handleEvent() {
        try {
            processReadyKeys();
        }
        catch(Exception e) {
            logger.info("I/O error at udp socket-reader: {}({})", e.getClass().getName(), e.getMessage());
        }
    }

    private void processReadyKeys() throws Exception {
        ownSelector.select();
        Set<SelectionKey> selectedKeys = this.ownSelector.selectedKeys();
        Iterator<SelectionKey> iterator = selectedKeys.iterator();
        while(iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if(key.isValid()) {
                processReadyKey(key);
            }
        }
    }

    private void processReadyKey(SelectionKey key) throws Exception {
        if(key.isReadable()) {
            processReadableKey(key);
        }
    }

    private void processReadableKey(SelectionKey key) throws Exception {
        DatagramChannel channel = (DatagramChannel) key.channel();
        try {
            processReadBytes(channel);
        }
        catch (ClosedSelectorException e) {
            logger.debug("selector has already closed: {}", e.getMessage());

        }
        catch (CancelledKeyException e) {
            logger.debug("key has already cancelled: {}", e.getMessage());
        }
        catch (IOException e) {
            logger.warn("io exception: {}", e.getMessage());

        }
        catch (Exception e) {
            logger.error("fatal error at udp socket-reader", e);
        }
    }

    private void processReadBytes(DatagramChannel channel) throws Exception {
        buffer.clear();
        InetSocketAddress address = (InetSocketAddress) channel.receive(buffer);
        if(address == null) {
            logger.info("has no data in udp channel: {}", channel);
            return;
        }
        int byteCount = buffer.position();

        if (byteCount > 0) {
            buffer.flip();
            byte[] binary = new byte[buffer.limit()];
            buffer.get(binary);
            EzyUdpReceivedPacket packet =
                    new EzySimpleUdpReceivedPacket(channel, address, binary);
            udpDataHandler.fireUdpPacketReceived(packet);
        }
    }
}
