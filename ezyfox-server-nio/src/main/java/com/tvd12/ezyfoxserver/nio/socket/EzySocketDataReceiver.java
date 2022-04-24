package com.tvd12.ezyfoxserver.nio.socket;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

import org.eclipse.jetty.websocket.api.Session;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyCoreConstants;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;

public class EzySocketDataReceiver extends EzyLoggable implements EzyDestroyable {

    protected final int threadPoolSize;
    protected final ByteBuffer tcpByteBuffers[];
    protected final ExecutorService[] executorServices;
    protected final EzyHandlerGroupManager handlerGroupManager;

    public EzySocketDataReceiver(Builder builder) {
        this.threadPoolSize = builder.threadPoolSize;
        this.handlerGroupManager = builder.handlerGroupManager;
        this.tcpByteBuffers = newTcpByteBuffers(threadPoolSize);
        this.executorServices = newExecutorServices(threadPoolSize);
    }

    private ByteBuffer[] newTcpByteBuffers(int size) {
        ByteBuffer[] answer = new ByteBuffer[size];
        for(int i = 0 ; i < size ; ++i)
            answer[i] = ByteBuffer.allocateDirect(getMaxBufferSize());
        return answer;
    }

    private ExecutorService[] newExecutorServices(int threadPoolSize) {
        ExecutorService[] answer = new ExecutorService[threadPoolSize];
        for(int i = 0 ; i < threadPoolSize ; ++i)
            answer[i] = EzyExecutors.newSingleThreadExecutor("socket-data-receiver");
        return answer;
    }

    public void tcpReceive(SocketChannel channel) {
        int index = Math.abs(channel.hashCode() % threadPoolSize);
        ByteBuffer buffer = tcpByteBuffers[index];
        ExecutorService executorService = executorServices[index];
        executorService.execute(() -> tcpReceive0(channel, buffer));
    }

    private void tcpReceive0(SocketChannel channel, ByteBuffer buffer) {
        try {
            tcpReadBytes(channel, buffer);
        }
        catch (Exception e) {
            logger.info("I/O error at tcp-data-reader (channel: {}): {}({})", channel, e.getClass().getName(), e.getMessage());
        }
    }

    private void tcpReadBytes(SocketChannel channel, ByteBuffer buffer) throws Exception {
        int readBytes = -1;
        Exception exception = null;
        try {
            buffer.clear();
            readBytes = channel.read(buffer);
        }
        catch (ClosedChannelException e) {
            // do nothing
        }
        catch (Exception e) {
            exception = e;
        }
        if(readBytes == -1) {
            tcpCloseConnection(channel);
        }
        else if(readBytes > 0) {
            processReadBytes(channel, buffer);
        }
        if(exception != null)
            throw exception;
    }

    private void processReadBytes(SocketChannel channel, ByteBuffer buffer) throws Exception {
        buffer.flip();
        byte[] binary = new byte[buffer.limit()];
        buffer.get(binary);
        EzyNioHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup(channel);
        if(handlerGroup != null)
            handlerGroup.fireBytesReceived(binary);
    }

    private void tcpCloseConnection(SocketChannel channel) {
        EzyHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup(channel);
        if(handlerGroup != null)
            handlerGroup.enqueueDisconnection();
    }

    public void udpReceive(Object socketChannel, EzyMessage message) {
        ExecutorService executorService = selectedExecutorService(socketChannel);
        executorService.execute(() -> udpReceive0(socketChannel, message));
    }

    private void udpReceive0(Object socketChannel, EzyMessage message) {
        try {
            EzyNioHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup(socketChannel);
            if(handlerGroup != null)
                handlerGroup.fireMessageReceived(message);
        }
        catch(Exception e) {
            logger.info("I/O error at udp-message-received (channel: {}): {}({})", socketChannel, e.getClass().getName(), e.getMessage());
        }

    }

    public void wsReceive(Session session, String message) {
        ExecutorService executorService = selectedExecutorService(session);
        executorService.execute(() -> wsReceive0(session, message));
    }

    private void wsReceive0(Session session, String message) {
        try {
            EzyWsHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup(session);
            if(handlerGroup != null)
                handlerGroup.fireBytesReceived(message);
        }
        catch(Exception e) {
            logger.info("I/O error at ws-message-received (session: {}): {}({})", session, e.getClass().getName(), e.getMessage());
        }
    }

    public void wsReceive(Session session, byte[] payload, int offset, int len) {
        ExecutorService executorService = selectedExecutorService(session);
        executorService.execute(() -> wsReceive0(session, payload, offset, len));
    }

    private void wsReceive0(Session session, byte[] payload, int offset, int len) {
        try {
            EzyWsHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup(session);
            if(handlerGroup != null)
                handlerGroup.fireBytesReceived(payload, offset, len);
        }
        catch(Exception e) {
            logger.info("I/O error at ws-message-received (session: {}): {}({})", session, e.getClass().getName(), e.getMessage());
        }
    }

    public void wsCloseConnection(Session session) {
        EzyWsHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup(session);
        if(handlerGroup != null)
            handlerGroup.enqueueDisconnection();
    }

    @Override
    public void destroy() {
        for(ExecutorService executorService : executorServices)
            executorService.shutdownNow();
    }

    private ExecutorService selectedExecutorService(Object channel) {
        int index = Math.abs(channel.hashCode() % threadPoolSize);
        return executorServices[index];
    }

    private int getMaxBufferSize() {
        return EzyCoreConstants.MAX_READ_BUFFER_SIZE;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements EzyBuilder<EzySocketDataReceiver> {
        protected int threadPoolSize;
        protected EzyHandlerGroupManager handlerGroupManager;

        public Builder threadPoolSize(int threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
            return this;
        }

        public Builder handlerGroupManager(EzyHandlerGroupManager handlerGroupManager) {
            this.handlerGroupManager = handlerGroupManager;
            return this;
        }

        @Override
        public EzySocketDataReceiver build() {
            return new EzySocketDataReceiver(this);
        }
    }
}
