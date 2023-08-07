package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.socket.EzyChannel;
import lombok.AllArgsConstructor;

import javax.net.ssl.SSLContext;
import java.nio.channels.SocketChannel;

@AllArgsConstructor
public class EzyNioSecureSocketAcceptor extends EzyNioSocketAcceptor {

    private final SSLContext sslContext;
    private final int sslHandshakeTimeout;

    @Override
    protected EzyChannel newChannel(SocketChannel clientChannel) {
        return new EzyNioSecureSocketChannel(
            clientChannel,
            sslContext,
            sslHandshakeTimeout
        );
    }
}
