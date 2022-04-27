package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroup;

public interface EzyHandlerGroup extends
    EzySocketDataHandlerGroup,
    EzySocketWriterGroup,
    EzyDestroyable {

    void fireExceptionCaught(Throwable throwable);

    void enqueueDisconnection(EzyConstant reason);

    default void enqueueDisconnection() {
        enqueueDisconnection(EzyDisconnectReason.UNKNOWN);
    }
}
