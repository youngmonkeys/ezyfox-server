package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import lombok.AllArgsConstructor;

import java.nio.channels.SocketChannel;

@AllArgsConstructor
public class EzyNioSecureSocketAcceptor extends EzyNioSocketAcceptor {

    private final EzySslContextProxy sslContextProxy;
    private final int sslHandshakeTimeout;
    private final int maxRequestSize;

    @Override
    protected EzyChannel newChannel(SocketChannel clientChannel) {
        return new EzyNioSecureSocketChannel(
            clientChannel,
            sslContextProxy,
            sslHandshakeTimeout,
            maxRequestSize
        );
    }
}
