package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;
import lombok.Getter;

public enum EzyEventType implements EzyConstant {

    SERVER_INITIALIZING(0),
    SERVER_READY(1),
    CLIENT_HANDSHAKE(10),
    USER_LOGIN(21),
    USER_ACCESS_APP(25),
    USER_ADDED(26),
    USER_REMOVED(27),
    USER_ACCESSED_APP(28),
    SESSION_REMOVED(35),
    STREAMING(36);

    @Getter
    private final int id;

    EzyEventType(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return toString();
    }
}
