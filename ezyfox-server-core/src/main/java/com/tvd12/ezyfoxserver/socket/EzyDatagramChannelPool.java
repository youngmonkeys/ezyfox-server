package com.tvd12.ezyfoxserver.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyRoundRobin;

public class EzyDatagramChannelPool extends EzyLoggable {

    protected final EzyRoundRobin<DatagramChannel> channels;

    public EzyDatagramChannelPool(int poolSize) {
        this.channels = new EzyRoundRobin<>(() -> newChannel(), poolSize);
    }

    protected DatagramChannel newChannel() {
        try {
            DatagramChannel chan = openChannel();
            chan.configureBlocking(false);
            chan.socket().setReuseAddress(true);
            return chan;
        }
        catch (Exception e) {
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
            }
            catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    public void register(Selector selector) {
        channels.forEach(channel -> {
            try {
                channel.register(selector, SelectionKey.OP_READ);
            }
            catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    public DatagramChannel getChannel() {
        DatagramChannel channel = channels.get();
        return channel;
    }

    public void close() {
        channels.forEach(channel -> {
            try {
                channel.close();
            }
            catch (Exception e) {
                logger.warn("close datagram channel: {} error", channel, e);
            }
        });
    }

}
