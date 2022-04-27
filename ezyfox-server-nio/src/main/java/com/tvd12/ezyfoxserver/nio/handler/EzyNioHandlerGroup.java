package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;

public interface EzyNioHandlerGroup extends EzyHandlerGroup {

    void fireBytesReceived(byte[] bytes) throws Exception;

    void fireMessageReceived(EzyMessage message) throws Exception;

    EzyNioSession getSession();
}
