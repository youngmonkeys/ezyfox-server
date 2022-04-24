package com.tvd12.ezyfoxserver.nio.websocket;

import static com.tvd12.ezyfoxserver.nio.websocket.EzyWsCloseStatus.CLOSE_BY_SERVER;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

import lombok.Getter;

@Getter
public class EzyWsChannel implements EzyChannel {

    private final Session session;
    private volatile boolean opened;
    private final SocketAddress serverAddress;
    private final SocketAddress clientAddress;
    private final static Logger LOGGER = LoggerFactory.getLogger(EzyWsChannel.class);

    public EzyWsChannel(Session session) {
        this.opened = true;
        this.session = session;
        this.serverAddress = session.getLocalAddress();
        this.clientAddress = session.getRemoteAddress();
    }

    @Override
    public int write(Object data, boolean binary) throws Exception {
        try {
            if(binary)
                return writeBinary((byte[])data);
            return writeString((String)data);
        }
        catch(WebSocketException e) {
            LOGGER.debug("write data: {}, to: {} error", data, clientAddress, e);
            return 0;
        }
    }

    private int writeBinary(byte[] bytes) throws Exception {
        int bytesSize = bytes.length;
        RemoteEndpoint remote = session.getRemote();
        remote.sendBytes(ByteBuffer.wrap(bytes));
        return bytesSize;
    }

    private int writeString(String bytes) throws Exception {
        int bytesSize = bytes.length();
        RemoteEndpoint remote = session.getRemote();
        remote.sendString(bytes);
        return bytesSize;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Session getConnection() {
        return session;
    }

    @Override
    public EzyConnectionType getConnectionType() {
        return EzyConnectionType.WEBSOCKET;
    }

    @Override
    public boolean isConnected() {
        return opened;
    }

    public void setClosed() {
        this.opened = false;
    }

    @Override
    public void disconnect() {
        try {
            session.disconnect();
        }
        catch(Exception e) {
            LOGGER.warn("disconnect session: {} error", session, e);
        }
    }

    @Override
    public void close() {
        try {
            session.close(CLOSE_BY_SERVER);
        }
        catch(Exception e) {
            LOGGER.warn("close session: {} error", session, e);
        }
    }
}
