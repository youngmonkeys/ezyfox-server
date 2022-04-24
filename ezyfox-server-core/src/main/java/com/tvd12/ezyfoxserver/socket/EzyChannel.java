package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

import java.net.SocketAddress;

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
