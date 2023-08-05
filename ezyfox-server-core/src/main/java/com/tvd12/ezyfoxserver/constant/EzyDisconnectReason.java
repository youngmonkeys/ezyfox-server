package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EzyDisconnectReason implements EzyConstant {

    UNKNOWN(0),
    IDLE(1),
    NOT_LOGGED_IN(2),
    ANOTHER_SESSION_LOGIN(3),
    ADMIN_BAN(4),
    ADMIN_KICK(5),
    MAX_REQUEST_PER_SECOND(6),
    MAX_REQUEST_SIZE(7),
    SERVER_ERROR(8),
    SSH_HANDSHAKE_FAILED(9);

    private static final Map<Integer, EzyDisconnectReason> REASONS_BY_ID = reasonsById();

    @Getter
    private final int id;

    EzyDisconnectReason(int id) {
        this.id = id;
    }

    public static EzyDisconnectReason valueOf(int id) {
        return REASONS_BY_ID.get(id);
    }

    private static Map<Integer, EzyDisconnectReason> reasonsById() {
        Map<Integer, EzyDisconnectReason> map = new ConcurrentHashMap<>();
        for (EzyDisconnectReason reason : values()) {
            map.put(reason.getId(), reason);
        }
        return map;
    }

    @Override
    public String getName() {
        return toString();
    }
}
