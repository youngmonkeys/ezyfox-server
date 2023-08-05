package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;
import lombok.Setter;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzyNioSocketReader extends EzySocketAbstractEventHandler {

    @Setter
    protected Selector ownSelector;
    @Setter
    protected EzySocketDataReceiver socketDataReceiver;
    @Setter
    protected EzyNioAcceptableConnectionsHandler acceptableConnectionsHandler;

    @Override
    public void destroy() {
        processWithLogException(() -> ownSelector.close());
    }

    @Override
    public void handleEvent() {
        try {
            acceptableConnectionsHandler.handleAcceptableConnections();
            int readyKeyCount = ownSelector.selectNow();
            if (readyKeyCount > 0) {
                processReadyKeys();
            }
            Thread.sleep(3L);
        } catch (Throwable e) {
            logger.info("I/O error at socket-reader", e);
        }
    }

    protected void processReadyKeys() {
        Set<SelectionKey> readyKeys = this.ownSelector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isValid()) {
                try {
                    processReadyKey(key);
                } catch (Throwable e) {
                    logger.info("process ready key: {} error", key, e);
                }
            }
        }
    }

    private void processReadyKey(SelectionKey key) {
        if (key.isWritable()) {
            key.interestOps(SelectionKey.OP_READ);
        }
        if (key.isReadable()) {
            processReadableChannel((SocketChannel) key.channel());
        }
    }

    protected void processReadableChannel(SocketChannel channel) {
        socketDataReceiver.tcpReceive(channel);
    }
}
