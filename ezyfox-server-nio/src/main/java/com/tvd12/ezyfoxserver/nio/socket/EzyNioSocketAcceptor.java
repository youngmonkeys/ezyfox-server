package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManagerAware;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;
import lombok.Setter;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzyNioSocketAcceptor
    extends EzySocketAbstractEventHandler
    implements EzyHandlerGroupManagerAware, EzyNioAcceptableConnectionsHandler {

    @Setter
    protected boolean tcpNoDelay;
    @Setter
    protected Selector ownSelector;
    @Setter
    protected Selector readSelector;
    @Setter
    protected EzyHandlerGroupManager handlerGroupManager;
    protected final List<SocketChannel> acceptableConnections =
        new ArrayList<>();
    protected final List<SocketChannel> acceptableConnectionsBuffer =
        new ArrayList<>();

    @Override
    public void handleEvent() {
        try {
            processReadyKeys();
        } catch (Throwable e) {
            logger.info("I/O error at socket-acceptor", e);
        }
    }

    private void processReadyKeys() throws Exception {
        ownSelector.select();

        Set<SelectionKey> readyKeys = ownSelector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            processReadyKey(key);
        }
        readSelector.wakeup();
    }

    private void processReadyKey(SelectionKey key) throws Exception {
        if (key.isAcceptable()) {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel clientChannel = serverChannel.accept();
            addConnection(clientChannel);
        }
    }

    private void addConnection(SocketChannel clientChannel) {
        synchronized (acceptableConnections) {
            acceptableConnections.add(clientChannel);
        }
    }

    public void handleAcceptableConnections() {
        synchronized (acceptableConnections) {
            acceptableConnectionsBuffer.addAll(acceptableConnections);
            acceptableConnections.clear();
        }
        try {
            for (SocketChannel clientChannel : acceptableConnectionsBuffer) {
                acceptConnection(clientChannel);
            }
        } finally {
            acceptableConnectionsBuffer.clear();
        }
    }

    private void acceptConnection(SocketChannel clientChannel) {
        try {
            doAcceptConnection(clientChannel);
        } catch (Throwable e) {
            logger.info("can't accept connection: {}", clientChannel, e);
        }
    }

    private void doAcceptConnection(
        SocketChannel clientChannel
    ) throws Exception {
        clientChannel.configureBlocking(false);
        clientChannel.socket().setTcpNoDelay(tcpNoDelay);
        SelectionKey selectionKey = clientChannel
            .register(readSelector, SelectionKey.OP_READ);
        EzyChannel channel = newChannel(clientChannel);
        EzyNioHandlerGroup handlerGroup = handlerGroupManager
            .newHandlerGroup(channel, EzyConnectionType.SOCKET);
        EzyNioSession session = handlerGroup.getSession();
        session.setProperty(EzyNioSession.SELECTION_KEY, selectionKey);
    }

    protected EzyChannel newChannel(SocketChannel clientChannel) {
        return new EzyNioSocketChannel(clientChannel);
    }

    @Override
    public void destroy() {
        synchronized (acceptableConnections) {
            acceptableConnections.clear();
        }
        processWithLogException(ownSelector::close);
    }
}
