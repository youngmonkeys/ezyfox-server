package com.tvd12.ezyfoxserver.nio.wrapper;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroupFetcher;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroupRemover;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroupFetcher;

import java.net.SocketAddress;

public interface EzyHandlerGroupManager
    extends EzySocketDataHandlerGroupFetcher,
    EzySocketDataHandlerGroupRemover,
    EzySocketWriterGroupFetcher,
    EzyDestroyable {

    <T extends EzyHandlerGroup> T getHandlerGroup(Object connection);

    <T extends EzyHandlerGroup> T newHandlerGroup(EzyChannel channel, EzyConnectionType type);

    void unmapHandlerGroup(SocketAddress udpAddress);

    void mapSocketChannel(SocketAddress udpAddress, EzySession session);

    Object getSocketChannel(SocketAddress udpAddress);
}
