package com.tvd12.ezyfoxserver.socket;

import java.net.SocketAddress;

public interface EzyChannel {

    void close();

    void disconnect();

    boolean isConnected();

    int write(Object data, boolean binary) throws Exception;

    <T> T getConnection();

    String getConnectionType();

    SocketAddress getServerAddress();

    SocketAddress getClientAddress();
}
