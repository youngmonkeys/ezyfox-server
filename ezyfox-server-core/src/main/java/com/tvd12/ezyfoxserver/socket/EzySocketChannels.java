package com.tvd12.ezyfoxserver.socket;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public final class EzySocketChannels {

    private EzySocketChannels() {}

    public static void write(
        SocketChannel socketChannel,
        ByteBuffer buffer,
        long timeoutAt
    ) throws IOException {
        buffer.flip();
        while (buffer.hasRemaining()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= timeoutAt) {
                throw new SSLException("Timeout");
            }
            socketChannel.write(buffer);
        }
    }
}
