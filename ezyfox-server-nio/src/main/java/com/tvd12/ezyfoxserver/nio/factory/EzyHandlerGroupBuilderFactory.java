package com.tvd12.ezyfoxserver.nio.factory;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

public interface EzyHandlerGroupBuilderFactory {

    EzyAbstractHandlerGroup.Builder newBuilder(
        EzyChannel channel,
        String type
    );

    default EzyAbstractHandlerGroup.Builder newBuilder(
        EzyChannel channel,
        EzyConnectionType type
    ) {
        return newBuilder(channel, type.getName());
    }
}
