package com.tvd12.ezyfoxserver.socket;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

public interface EzyChannel {

    void close();

    void disconnect();

    boolean isConnected();

    int write(Object data, boolean binary) throws Exception;

    <T> T getConnection();

    EzyConnectionType getConnectionType();

    SocketAddress getServerAddress();

    SocketAddress getClientAddress();

}
