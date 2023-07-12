package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManagerAware;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeHandler;
import lombok.Setter;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

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
    protected SSLContext sslContext;
    @Setter
    protected EzySslHandshakeHandler sslHandshakeHandler;
    protected final List<SocketChannel> acceptableConnections;
    protected final List<SocketChannel> acceptableConnectionsBuffer;
    protected final ExecutorService connectionAcceptorExecutorService;

    public EzyNioSocketAcceptor(
        int connectionAcceptorThreadPoolSize
    ) {
        acceptableConnections = new ArrayList<>();
        acceptableConnectionsBuffer = new ArrayList<>();
        connectionAcceptorExecutorService = EzyExecutors.newFixedThreadPool(
            connectionAcceptorThreadPoolSize,
            "connection-acceptor"
        );
    }

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

    public void handleAcceptableConnections() throws Exception {
        synchronized (acceptableConnections) {
            acceptableConnectionsBuffer.addAll(acceptableConnections);
            acceptableConnections.clear();
        }
        CountDownLatch countDownLatch = new CountDownLatch(
            acceptableConnectionsBuffer.size()
        );
        try {
            for (SocketChannel clientChannel : acceptableConnectionsBuffer) {
                connectionAcceptorExecutorService.execute(() -> {
                    acceptConnection(clientChannel);
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
        } finally {
            acceptableConnectionsBuffer.clear();
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

    private void acceptConnection(SocketChannel clientChannel) {
        try {
            doAcceptConnection(clientChannel);
        } catch (Throwable e) {
            logger.info("can't accept connection: {}", clientChannel, e);
        }
    }

    private void doAcceptConnection(SocketChannel clientChannel) throws Exception {
        clientChannel.configureBlocking(false);
        clientChannel.socket().setTcpNoDelay(tcpNoDelay);
        SSLEngine sslEngine = null;
        try {
            if (sslHandshakeHandler != null) {
                sslEngine = sslHandshakeHandler.handle(clientChannel);
            }
        } catch (Exception e) {
            clientChannel.close();
            throw e;
        }
        EzyChannel channel = sslEngine == null
            ? new EzyNioSocketChannel(clientChannel)
            : new EzyNioSecureSocketChannel(clientChannel, sslEngine);

        EzyNioHandlerGroup handlerGroup = handlerGroupManager
            .newHandlerGroup(channel, EzyConnectionType.SOCKET);
        EzyNioSession session = handlerGroup.getSession();

        SelectionKey selectionKey = clientChannel.register(readSelector, SelectionKey.OP_READ);
        session.setProperty(EzyNioSession.SELECTION_KEY, selectionKey);
    }
}
