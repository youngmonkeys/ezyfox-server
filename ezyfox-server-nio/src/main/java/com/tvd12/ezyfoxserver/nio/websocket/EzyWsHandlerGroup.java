package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;

public interface EzyWsHandlerGroup extends EzyHandlerGroup {

    void fireBytesReceived(String bytes) throws Exception;

    void fireBytesReceived(byte[] bytes, int offset, int len) throws Exception;

}
