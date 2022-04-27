package com.tvd12.ezyfoxserver.nio.wrapper.impl;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroup;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EzyHandlerGroupManagerImpl
    extends EzyLoggable
    implements EzyHandlerGroupManager {

    private final Map<Object, Object> socketChannelByUdpAddress;
    private final Map<Object, EzyHandlerGroup> groupsByConnection;
    private final EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory;

    public EzyHandlerGroupManagerImpl(Builder builder) {
        this.handlerGroupBuilderFactory = builder.handlerGroupBuilderFactory;
        this.groupsByConnection = new ConcurrentHashMap<>();
        this.socketChannelByUdpAddress = new ConcurrentHashMap<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EzyHandlerGroup> T newHandlerGroup(
        EzyChannel channel,
        EzyConnectionType type
    ) {
        EzyHandlerGroup group = handlerGroupBuilderFactory
            .newBuilder(channel, type)
            .build();
        groupsByConnection.put(channel.getConnection(), group);
        return (T) group;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EzyHandlerGroup> T getHandlerGroup(Object connection) {
        return (T) groupsByConnection.get(connection);
    }

    private EzyHandlerGroup getHandlerGroup(EzySession session) {
        if (session == null) {
            return null;
        }
        EzyChannel channel = session.getChannel();
        if (channel == null) {
            return null;
        }
        Object connection = channel.getConnection();
        if (connection == null) {
            return null;
        }
        return groupsByConnection.get(connection);
    }

    @Override
    public void unmapHandlerGroup(SocketAddress udpAddress) {
        if (udpAddress != null) {
            socketChannelByUdpAddress.remove(udpAddress);
            logger.debug(
                "unmap socket channel from: {}, socketChannelByUdpAddress.size: {}",
                udpAddress,
                socketChannelByUdpAddress.size()
            );
        }
    }

    @Override
    public void mapSocketChannel(
        SocketAddress udpAddress,
        EzySession session
    ) {
        if (session == null) {
            return;
        }
        EzyChannel channel = session.getChannel();
        if (channel == null) {
            return;
        }
        Object connection = channel.getConnection();
        if (connection == null) {
            return;
        }
        if (groupsByConnection.containsKey(connection)) {
            socketChannelByUdpAddress.put(udpAddress, connection);
        }
        logger.debug(
            "map socket channel to: {}, socketChannelByUdpAddress.size: {}",
            udpAddress,
            socketChannelByUdpAddress.size()
        );
    }

    @Override
    public Object getSocketChannel(SocketAddress udpAddress) {
        return socketChannelByUdpAddress.get(udpAddress);
    }

    @Override
    public EzySocketDataHandlerGroup removeHandlerGroup(EzySession session) {
        if (session == null) {
            return null;
        }
        EzyChannel channel = session.getChannel();
        if (channel == null) {
            return null;
        }
        Object connection = channel.getConnection();
        if (connection == null) {
            return null;
        }
        EzyHandlerGroup group = groupsByConnection.remove(connection);
        SocketAddress udpClientAddress = session.getUdpClientAddress();
        if (udpClientAddress != null) {
            socketChannelByUdpAddress.remove(udpClientAddress);
        }
        logger.debug(
            "remove handler group: {} with session: {}, " +
                "groupsByConnection.size: {}, socketChannelByUdpAddress.size: {}",
            group,
            session,
            groupsByConnection.size(),
            socketChannelByUdpAddress.size()
        );
        return group;
    }

    @Override
    public EzySocketDataHandlerGroup getDataHandlerGroup(EzySession session) {
        return getHandlerGroup(session);
    }

    @Override
    public EzySocketWriterGroup getWriterGroup(EzySession session) {
        return getHandlerGroup(session);
    }

    @Override
    public void destroy() {
        groupsByConnection.clear();
    }

    public static class Builder implements EzyBuilder<EzyHandlerGroupManager> {
        private EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory;

        public Builder handlerGroupBuilderFactory(EzyHandlerGroupBuilderFactory factory) {
            this.handlerGroupBuilderFactory = factory;
            return this;
        }

        @Override
        public EzyHandlerGroupManager build() {
            return new EzyHandlerGroupManagerImpl(this);
        }
    }
}
