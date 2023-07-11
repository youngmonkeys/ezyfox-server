package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManagerAware;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketAbstractEventHandler;
import lombok.Setter;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;

public class EzyNioSocketAcceptor
    extends EzySocketAbstractEventHandler
    implements EzyHandlerGroupManagerAware, EzyNioAcceptableConnectionsHandler {

    @Setter
    protected SSLContext sslContext;
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
            logger.error("can't accept connection: {}", clientChannel, e);
        }
    }

    private void doAcceptConnection(SocketChannel clientChannel) throws Exception {
        clientChannel.configureBlocking(false);
        clientChannel.socket().setTcpNoDelay(tcpNoDelay);
        try {
            handleSslHandshake(clientChannel);
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

    protected EzyNioHandlerGroup newHandlerGroup(EzyChannel channel) {
        return handlerGroupManager
            .newHandlerGroup(channel, EzyConnectionType.SOCKET);
    }

    @SuppressWarnings("MethodLength")
    protected void handleSslHandshake(SocketChannel socketChannel) throws IOException {
        SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        engine.beginHandshake();

        SSLSession session = engine.getSession();
        int appBufferSize = session.getApplicationBufferSize();
        int packetBufferSize = session.getPacketBufferSize();
        ByteBuffer myAppData = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerNetData = ByteBuffer.allocate(packetBufferSize);
        ByteBuffer myNetData = ByteBuffer.allocate(packetBufferSize);

        SSLEngineResult result;
        SSLException sslException = null;
        SSLEngineResult.HandshakeStatus handshakeStatus = engine.getHandshakeStatus();
        while (handshakeStatus != FINISHED && handshakeStatus != NOT_HANDSHAKING) {
            switch (handshakeStatus) {
                case NEED_UNWRAP:
                    if (socketChannel.read(peerNetData) < 0) {
                        if (engine.isInboundDone() && engine.isOutboundDone()) {
                            throw new SSLException(
                                "status is NEED_UNWRAP " +
                                    "while inbound and outbound done"
                            );
                        }
                        try {
                            engine.closeInbound();
                        } catch (SSLException e) {
                            sslException = e;
                            logger.info(
                                "This engine was forced to close inbound, " +
                                    "without having received the proper SSL/TLS close " +
                                    "notification message from the peer, due to end of stream."
                            );
                        }
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        break;
                    }
                    peerNetData.flip();
                    try {
                        result = engine.unwrap(peerNetData, peerAppData);
                        peerNetData.compact();
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException e) {
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        sslException = e;
                        logger.error(
                            "A problem was encountered while processing the data " +
                                "that caused the SSLEngine to abort. " +
                                "Will try to properly close connection..."
                        );
                        break;
                    }
                    switch (result.getStatus()) {
                        case OK:
                            break;
                        case BUFFER_OVERFLOW:
                            peerAppData = enlargeApplicationBuffer(engine, peerAppData);
                            break;
                        case BUFFER_UNDERFLOW:
                            peerNetData = handleBufferUnderflow(engine, peerNetData);
                            break;
                        case CLOSED:
                            if (engine.isOutboundDone()) {
                                throw new SSLException("status CLOSED while outbound done");
                            } else {
                                engine.closeOutbound();
                                handshakeStatus = engine.getHandshakeStatus();
                                break;
                            }
                        default:
                            throw new IllegalStateException(
                                "Invalid SSL status: " + result.getStatus()
                            );
                    }
                    break;
                case NEED_WRAP:
                    myNetData.clear();
                    try {
                        result = engine.wrap(myAppData, myNetData);
                        handshakeStatus = result.getHandshakeStatus();
                    } catch (SSLException e) {
                        engine.closeOutbound();
                        handshakeStatus = engine.getHandshakeStatus();
                        sslException = e;
                        logger.error(
                            "A problem was encountered while processing the data " +
                                "that caused the SSLEngine to abort. " +
                                "Will try to properly close connection..."
                        );
                        break;
                    }
                    switch (result.getStatus()) {
                        case OK :
                            myNetData.flip();
                            while (myNetData.hasRemaining()) {
                                socketChannel.write(myNetData);
                            }
                            break;
                        case BUFFER_OVERFLOW:
                            myNetData = enlargePacketBuffer(engine, myNetData);
                            break;
                        case BUFFER_UNDERFLOW:
                            throw new SSLException("Buffer underflow occurred after a wrap.");
                        case CLOSED:
                            try {
                                myNetData.flip();
                                while (myNetData.hasRemaining()) {
                                    socketChannel.write(myNetData);
                                }
                                peerNetData.clear();
                            } catch (Exception e) {
                                logger.error(
                                    "Failed to send server's close message " +
                                        "due to socket channel's failure."
                                );
                                handshakeStatus = engine.getHandshakeStatus();
                            }
                            break;
                        default:
                            throw new SSLException(
                                "Invalid SSL status: " + result.getStatus()
                            );
                    }
                    break;
                case NEED_TASK:
                    Runnable task;
                    while ((task = engine.getDelegatedTask()) != null) {
                        task.run();
                    }
                    handshakeStatus = engine.getHandshakeStatus();
                    break;
                default:
                    throw new IllegalStateException("Invalid SSL status: " + handshakeStatus);
            }
        }
        if (sslException != null) {
            throw sslException;
        }
    }

    protected ByteBuffer handleBufferUnderflow(
        SSLEngine engine,
        ByteBuffer buffer
    ) {
        if (engine.getSession().getPacketBufferSize() < buffer.limit()) {
            return buffer;
        } else {
            ByteBuffer replaceBuffer = enlargePacketBuffer(engine, buffer);
            buffer.flip();
            replaceBuffer.put(buffer);
            return replaceBuffer;
        }
    }

    protected ByteBuffer enlargePacketBuffer(
        SSLEngine engine,
        ByteBuffer buffer
    ) {
        return enlargeBuffer(
            buffer,
            engine.getSession().getPacketBufferSize()
        );
    }

    protected ByteBuffer enlargeApplicationBuffer(
        SSLEngine engine,
        ByteBuffer buffer
    ) {
        return enlargeBuffer(
            buffer,
            engine.getSession().getApplicationBufferSize()
        );
    }

    protected ByteBuffer enlargeBuffer(
        ByteBuffer buffer,
        int sessionProposedCapacity
    ) {
        if (sessionProposedCapacity > buffer.capacity()) {
            buffer = ByteBuffer.allocate(sessionProposedCapacity);
        } else {
            buffer = ByteBuffer.allocate(buffer.capacity() * 2);
        }
        return buffer;
    }
}
