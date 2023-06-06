package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyRoundRobin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class EzyDatagramChannelPool extends EzyLoggable {

    protected final EzyRoundRobin<DatagramChannel> channels;

    public EzyDatagramChannelPool(int poolSize) {
        this.channels = new EzyRoundRobin<>(this::newChannel, poolSize);
    }

    protected DatagramChannel newChannel() {
        try {
            DatagramChannel channel = openChannel();
            channel.configureBlocking(false);
            channel.socket().setReuseAddress(true);
            return channel;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    protected DatagramChannel openChannel() throws IOException {
        return DatagramChannel.open();
    }

    public void bind(InetSocketAddress address) {
        channels.forEach(channel -> {
            try {
                channel.bind(address);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        });
    }

    public void register(Selector selector) {
        channels.forEach(channel -> {
            try {
                channel.register(selector, SelectionKey.OP_READ);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        });
    }

    public DatagramChannel getChannel() {
        return channels.get();
    }

    public void close() {
        channels.forEach(channel -> {
            try {
                channel.close();
            } catch (Throwable e) {
                logger.warn("close datagram channel: {} error", channel, e);
            }
        });
    }
}
