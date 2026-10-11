package com.tvd12.ezyfoxserver.protocol;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

public interface EzyProtocolChannel extends EzyChannel {

    Object encode(EzyArray data) throws Exception;

    default boolean isBinary() {
        return true;
    }
}
