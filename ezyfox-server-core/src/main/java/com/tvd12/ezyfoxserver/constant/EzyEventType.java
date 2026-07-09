package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyEnums;
import lombok.Getter;

import java.util.Map;

@Getter
public enum EzyEventType implements EzyConstant {

    SERVER_INITIALIZING(0),
    SERVER_READY(1),
    CLIENT_HANDSHAKE(10),
    USER_LOGIN(21),
    USER_ACCESS_APP(25),
    USER_ADDED(26),
    USER_REMOVED(27),
    USER_ACCESSED_APP(28),
    USER_EXITED_APP(29),
    SESSION_REMOVED(35),
    STREAMING(36);

    private final int id;

    private static final Map<String, EzyEventType> MAP_BY_NAME = EzyEnums
        .enumMap(EzyEventType.class, EzyEventType::toString);

    EzyEventType(int id) {
        this.id = id;
    }

    public static EzyEventType of(String name) {
        return name == null ? null : MAP_BY_NAME.get(name);
    }

    @Override
    public String getName() {
        return toString();
    }
}
