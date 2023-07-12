package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.constant.SslType;
import com.tvd12.ezyfoxserver.nio.constant.EzyNioThreadPoolSizes;
import com.tvd12.ezyfoxserver.nio.socket.*;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopOneHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketWriter;
import com.tvd12.ezyfoxserver.socket.EzySocketWritingLoopHandler;
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeHandler;

import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzySocketServerBootstrap extends EzyAbstractSocketServerBootstrap {

    private Selector readSelector;
    private Selector acceptSelector;
    private ServerSocket serverSocket;
    private ServerSocketChannel serverSocketChannel;
    private EzySocketEventLoopHandler readingLoopHandler;
    private EzySocketEventLoopHandler socketAcceptanceLoopHandler;
    private final SSLContext sslContext;

    public EzySocketServerBootstrap(Builder builder) {
        super(builder);
        this.sslContext = builder.sslContext;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void start() throws Exception {
        openSelectors();
        newAndConfigServerSocketChannel();
        getBindAndConfigServerSocket();
        startSocketHandlers();
    }

    @Override
    public void destroy() {
        processWithLogException(() -> writingLoopHandler.destroy());
        processWithLogException(() -> readingLoopHandler.destroy());
        processWithLogException(() -> socketAcceptanceLoopHandler.destroy());
        processWithLogException(() -> serverSocket.close());
        processWithLogException(() -> serverSocketChannel.close());
    }

    private void openSelectors() throws Exception {
        this.readSelector = openSelector();
        this.acceptSelector = openSelector();
    }

    private void newAndConfigServerSocketChannel() throws Exception {
        this.serverSocketChannel = newServerSocketChannel();
        this.serverSocketChannel.configureBlocking(false);
    }

    private void getBindAndConfigServerSocket() throws Exception {
        this.serverSocket = serverSocketChannel.socket();
        this.serverSocket.setReuseAddress(true);
        this.serverSocket.bind(new InetSocketAddress(getSocketAddress(), getSocketPort()));
        this.serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
    }

    private void startSocketHandlers() throws Exception {
        EzyNioSocketAcceptor socketAcceptor = newSocketAcceptor();
        writingLoopHandler = newWritingLoopHandler();
        readingLoopHandler = newReadingLoopHandler(socketAcceptor);
        socketAcceptanceLoopHandler = newSocketAcceptanceLoopHandler(socketAcceptor);
        socketAcceptanceLoopHandler.start();
        readingLoopHandler.start();
        writingLoopHandler.start();
    }

    private EzyNioSocketAcceptor newSocketAcceptor() {
        EzyNioSocketAcceptor acceptor;
        if (isEnableL4Ssl()) {
            acceptor = new EzyNioSocketAcceptor(
                getSslConnectionAcceptorThreadPoolSize()
            );
            acceptor.setSslHandshakeHandler(
                new EzySslHandshakeHandler(
                    sslContext,
                    getSslHandshakeTimeout()
                )
            );
        } else {
            acceptor = new EzyNioSocketAcceptor(
                getConnectionAcceptorThreadPoolSize()
            );
        }
        return acceptor;
    }

    private EzySocketEventLoopHandler newWritingLoopHandler() {
        EzySocketWritingLoopHandler loopHandler = new EzySocketWritingLoopHandler();
        loopHandler.setThreadPoolSize(getSocketWriterThreadPoolSize());
        loopHandler.setEventHandlerSupplier(() -> {
            EzySocketWriter eventHandler = new EzyNioSocketWriter();
            eventHandler.setWriterGroupFetcher(handlerGroupManager);
            eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
            return eventHandler;
        });
        return loopHandler;
    }

    private EzySocketEventLoopHandler newReadingLoopHandler(
        EzyNioAcceptableConnectionsHandler acceptableConnectionsHandler
    ) {
        EzySocketEventLoopOneHandler loopHandler = new EzyNioSocketReadingLoopHandler();
        loopHandler.setThreadPoolSize(getSocketReaderThreadPoolSize());
        EzyNioSocketReader eventHandler = new EzyNioSocketReader();
        eventHandler.setOwnSelector(readSelector);
        eventHandler.setSocketDataReceiver(socketDataReceiver);
        eventHandler.setAcceptableConnectionsHandler(acceptableConnectionsHandler);
        loopHandler.setEventHandler(eventHandler);
        return loopHandler;
    }

    private EzySocketEventLoopHandler newSocketAcceptanceLoopHandler(
        EzyNioSocketAcceptor socketAcceptor) {
        EzySocketEventLoopOneHandler loopHandler = new EzyNioSocketAcceptanceLoopHandler();
        loopHandler.setThreadPoolSize(getSocketAcceptorThreadPoolSize());
        socketAcceptor.setTcpNoDelay(getSocketTcpNoDelay());
        socketAcceptor.setReadSelector(readSelector);
        socketAcceptor.setOwnSelector(acceptSelector);
        socketAcceptor.setHandlerGroupManager(handlerGroupManager);
        loopHandler.setEventHandler(socketAcceptor);
        return loopHandler;
    }

    private Selector openSelector() throws Exception {
        return Selector.open();
    }

    private ServerSocketChannel newServerSocketChannel() throws Exception {
        return ServerSocketChannel.open();
    }

    private boolean isEnableL4Ssl() {
        EzySocketSetting setting = getSocketSetting();
        return setting.isSslActive() && setting.getSslType() == SslType.L4;
    }

    public int getSslHandshakeTimeout() {
        return getSocketSetting().getSslHandshakeTimeout();
    }

    private int getSocketReaderThreadPoolSize() {
        return EzyNioThreadPoolSizes.SOCKET_READER;
    }

    private int getSocketWriterThreadPoolSize() {
        return getSocketSetting().getWriterThreadPoolSize();
    }

    private int getConnectionAcceptorThreadPoolSize() {
        return getSocketSetting().getConnectionAcceptorThreadPoolSize();
    }

    private int getSslConnectionAcceptorThreadPoolSize() {
        return getSocketSetting().getSslConnectionAcceptorThreadPoolSize();
    }

    private int getSocketAcceptorThreadPoolSize() {
        return EzyNioThreadPoolSizes.SOCKET_ACCEPTOR;
    }

    private int getSocketPort() {
        return getSocketSetting().getPort();
    }

    private String getSocketAddress() {
        return getSocketSetting().getAddress();
    }

    private boolean getSocketTcpNoDelay() {
        return getSocketSetting().isTcpNoDelay();
    }

    private EzySocketSetting getSocketSetting() {
        return getServerSettings().getSocket();
    }

    public static class Builder
        extends EzyAbstractSocketServerBootstrap.Builder<Builder, EzySocketServerBootstrap> {

        private SSLContext sslContext;

        public Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        @Override
        public EzySocketServerBootstrap build() {
            return new EzySocketServerBootstrap(this);
        }
    }
}
