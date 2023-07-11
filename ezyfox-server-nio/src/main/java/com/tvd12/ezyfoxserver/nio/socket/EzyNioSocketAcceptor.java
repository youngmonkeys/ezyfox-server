package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManagerAware;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeHandler;
import lombok.Setter;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
    protected List<SocketChannel> acceptableConnections;
    @Setter
    protected EzyHandlerGroupManager handlerGroupManager;
    @Setter
    protected EzySslHandshakeHandler sslHandshakeHandler;

    @Override
    public void destroy() {
        processWithLogException(ownSelector::close);
    }

    @Override
    public void handleEvent() {
        try {
            processReadyKeys();
        } catch (Throwable e) {
            logger.info("I/O error at socket-acceptor", e);
        }
    }

    public void handleAcceptableConnections() {
        if (acceptableConnections.size() > 0) {
            //noinspection SynchronizeOnNonFinalField
            synchronized (acceptableConnections) {
                doHandleAcceptableConnections();
            }
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
        //noinspection SynchronizeOnNonFinalField
        synchronized (acceptableConnections) {
            acceptableConnections.add(clientChannel);
        }
    }

    private void doHandleAcceptableConnections() {
        Iterator<SocketChannel> iterator = acceptableConnections.iterator();
        while (iterator.hasNext()) {
            SocketChannel clientChannel = iterator.next();
            iterator.remove();
            acceptConnection(clientChannel);
        }
    }

    private void acceptConnection(SocketChannel clientChannel) {
        try {
            doAcceptConnection(clientChannel);
        } catch (Exception e) {
            logger.info("can't accept connection: {}", clientChannel, e);
        }
    }

    private void doAcceptConnection(SocketChannel clientChannel) throws Exception {
        clientChannel.configureBlocking(false);
        clientChannel.socket().setTcpNoDelay(tcpNoDelay);
        try {
            if (sslHandshakeHandler != null) {
                sslHandshakeHandler.handle(clientChannel);
            }
        } catch (Exception e) {
            clientChannel.close();
            throw e;
        }
        EzyChannel channel = new EzyNioSocketChannel(clientChannel);

        EzyNioHandlerGroup handlerGroup = handlerGroupManager
            .newHandlerGroup(channel, EzyConnectionType.SOCKET);
        EzyNioSession session = handlerGroup.getSession();

        SelectionKey selectionKey = clientChannel.register(readSelector, SelectionKey.OP_READ);
        session.setProperty(EzyNioSession.SELECTION_KEY, selectionKey);
    }
}
