package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.socket.EzyChannel;

import java.nio.channels.SocketChannel;

public class EzyNioSecureSocketAcceptor extends EzyNioSocketAcceptor {

    @Override
    protected EzyChannel newChannel(SocketChannel clientChannel) {
        return new EzyNioSecureSocketChannel(clientChannel);
    }
}
